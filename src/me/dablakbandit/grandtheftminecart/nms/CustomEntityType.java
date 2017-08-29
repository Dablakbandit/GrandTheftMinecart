package me.dablakbandit.grandtheftminecart.nms;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.logging.Level;

import me.dablakbandit.dabcore.utils.NMSUtils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityTargetEvent;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class CustomEntityType implements Listener{

	private static CustomEntityType instance = new CustomEntityType();

	public static CustomEntityType getInstance(){
		return instance;
	}

	private CustomEntityType(){
		
	}

	public IEntities getEntities(){
		return ie;
	}

	private IEntities ie = new IEntities();

	public void registerEntities() {
		for(IEntity e : ie.getEntities())a(e.getCustomClass(), e.getName(), e.getID());
		Class<?> biomebase = NMSUtils.getNMSClass("BiomeBase");
		Class<?> biomemeta;
		try{
			biomemeta = Class.forName("net.minecraft.server." + NMSUtils.getVersion() + "BiomeMeta");
		}catch(Exception e){
			biomemeta = NMSUtils.getInnerClass(biomebase, "BiomeMeta");
		}
		try{
			Object[] o = (Object[]) NMSUtils.getField(biomebase, "biomes").get(null);
			for(Object biomeBase : o){
				if(biomeBase==null)break;
				for(String field : new String[] { "at", "au", "av", "aw" }){
					try{
						Field list = NMSUtils.getField(biomebase, field);
						List<?> mobList = (List<?>)list.get(biomeBase);
						for(Object meta : mobList){
							for(IEntity e : ie.getEntities()){
								Field b = NMSUtils.getField(biomemeta, "b");
								if(e.getNMSClass().equals(b.get(meta))){
									b.set(meta, e.getCustomClass());
								}
							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void unregisterEntities(){
		try{
			for(IEntity e : ie.getEntities()){
				Class<?> type = NMSUtils.getNMSClass("EntityTypes");
				((Map) NMSUtils.getField(type, "d").get(null)).remove(e.getCustomClass());
				((Map) NMSUtils.getField(type, "f").get(null)).remove(e.getCustomClass());
				a(e.getNMSClass(), e.getName(), e.getID());
			}
			Class<?> biomebase = NMSUtils.getNMSClass("BiomeBase");
			Class<?> biomemeta;
			try{
				biomemeta = Class.forName("net.minecraft.server." + NMSUtils.getVersion() + "BiomeMeta");
			}catch(Exception e){
				biomemeta = NMSUtils.getInnerClass(biomebase, "BiomeMeta");
			}
			Object[] o = (Object[]) NMSUtils.getField(biomebase, "biomes").get(null);
			for(Object biomeBase : o){
				if(biomeBase==null)break;
				for(String field : new String[] { "at", "au", "av", "aw" }){
					try{
						Field list = NMSUtils.getField(biomebase, field);
						List<?> mobList = (List<?>)list.get(biomeBase);
						for(Object meta : mobList){
							for(IEntity e : ie.getEntities()){
								Field b = NMSUtils.getField(biomemeta, "b");
								if(e.getCustomClass().equals(b.get(meta))){
									b.set(meta, e.getNMSClass());
								}
							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void a(Class<?> custom, String name, int id){
		try {
			Class<?> type = NMSUtils.getNMSClass("EntityTypes");
			((Map) NMSUtils.getField(type, "d").get(null)).put(custom,name);
			((Map) NMSUtils.getField(type, "f").get(null)).put(custom,Integer.valueOf(id));
			((Map) NMSUtils.getField(type, "c").get(null)).put(name,custom);
			((Map) NMSUtils.getField(type, "e").get(null)).put(Integer.valueOf(id), custom);
			((Map) NMSUtils.getField(type, "g").get(null)).put(name,Integer.valueOf(id));
		} catch (Exception exc) {
			Bukkit.getLogger().log(Level.WARNING,name + " : " + custom.getSimpleName()+ " is unable to register");
		}
	}

	private Class<?> nmsentity = NMSUtils.getNMSClass("Entity"), world = NMSUtils.getNMSClass("World"), nmsinsen = NMSUtils.getNMSClass("EntityInsentient");
	private Method setloc = NMSUtils.getMethod(nmsentity, "setLocation", double.class, double.class, double.class, float.class, float.class),
			addent = NMSUtils.getMethod(world, "addEntity", nmsentity, SpawnReason.class),
			setgoalt = NMSUtils.getMethod(nmsinsen, "setGoalTarget", NMSUtils.getNMSClass("EntityLiving"), EntityTargetEvent.TargetReason.class, boolean.class);

	private Object spawnPolice(IEntity ientity, Location loc){
		try{
			Object entity = ientity.getCustomClass().getConstructor(world).newInstance(NMSUtils.getHandle(loc.getWorld()));
			setloc.invoke(entity, loc.getX(), loc.getY(), loc.getBlockZ(), loc.getPitch(), loc.getYaw());
			addent.invoke(NMSUtils.getHandle(loc.getWorld()), entity, SpawnReason.NATURAL);
			return entity;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public void spawnPolice(Player p, int level){
		try{
			Random r = new Random(p.getPlayerTime());
			List<Location> locs = getCircle(p.getLocation(), 10);
			Map<IEntity, Integer> spawns = StarsType.values()[level-1].getSpawns();
			for(Entry<IEntity, Integer> e : spawns.entrySet()){
				for(int i = 0; i < e.getValue(); i++){
					Object e1 = spawnPolice(e.getKey(), getTopLocation(locs.get(r.nextInt(locs.size() - 1))));
					setgoalt.invoke(e1, NMSUtils.getHandle(p), null, false);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void spawnPolice(Player p, Location loc, int level){
		try{
			Map<IEntity, Integer> spawns = StarsType.values()[level-1].getSpawns();
			for(Entry<IEntity, Integer> e : spawns.entrySet()){
				for(int i = 0; i < e.getValue(); i++){
					Object e1 = spawnPolice(e.getKey(), loc);
					setgoalt.invoke(e1, NMSUtils.getHandle(p), null, false);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public List<Location> getCircle(Location centerLoc, int radius) { 
		List<Location> circle = new ArrayList<Location>();
		World world = centerLoc.getWorld();
		int x = 0;
		int z = radius;
		int error = 0;
		int d = 2 - 2 * radius;
		while (z >= 0) {
			circle.add(new Location(world, centerLoc.getBlockX() + x, centerLoc.getY(), centerLoc.getBlockZ() + z));
			circle.add(new Location(world, centerLoc.getBlockX() - x, centerLoc.getY(), centerLoc.getBlockZ() + z));
			circle.add(new Location(world, centerLoc.getBlockX() - x, centerLoc.getY(), centerLoc.getBlockZ() - z));
			circle.add(new Location(world, centerLoc.getBlockX() + x, centerLoc.getY(), centerLoc.getBlockZ() - z));
			error = 2 * (d + z) - 1;
			if((d<0)&&(error<=0)){
				x++;
				d += 2 * x + 1;
			}else{
				error = 2 * (d - x) - 1;
				if((d > 0)&&(error>0)){
					z--;
					d += 1 - 2 * z;
				}else{
					x++;
					d += 2 * (x - z);
					z--;
				}
			}
		}
		return circle;
	}

	public static Location getTopLocation(Location loc){
		return loc.getWorld().getHighestBlockAt(loc).getLocation().add(0, 0, 1);
	}
}
