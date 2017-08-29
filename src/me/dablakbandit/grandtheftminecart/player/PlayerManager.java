package me.dablakbandit.grandtheftminecart.player;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import be.maximvdw.placeholderapi.PlaceholderReplacer;
import me.dablakbandit.dabcore.utils.NMSUtils;
import me.dablakbandit.grandtheftminecart.GrandTheftMinecart;
import me.dablakbandit.grandtheftminecart.nms.CustomEntityType;
import me.dablakbandit.grandtheftminecart.nms.StarsType;
import me.dablakbandit.grandtheftminecart.player.listener.CrackshotListener;

public class PlayerManager implements Listener{
	
	private static PlayerManager manager = new PlayerManager();
	
	public static PlayerManager getInstance(){
		return manager;
	}
	
	private Map<String, Players> players = new HashMap<String, Players>();
	
	@SuppressWarnings("deprecation")
	private PlayerManager(){
		Condition.ATTACKENTITY.getLevel();
		for(Player player : Bukkit.getOnlinePlayers())
			add(player);
		Bukkit.getPluginManager().registerEvents(this, GrandTheftMinecart.getInstance());
		if(Bukkit.getPluginManager().getPlugin("CrackShot") != null){
			Bukkit.getPluginManager().registerEvents(new CrackshotListener(), GrandTheftMinecart.getInstance());
		}
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(GrandTheftMinecart.getInstance(), new Runnable(){
			@Override
			public void run(){
				for(Entry<String, Players> e : players.entrySet()){
					Players pl = e.getValue();
					if(pl.getCooldown() == 0){
						pl.setWantedLevel(0);
						pl.setInfringe(0);
					}else{
						pl.setCooldown(pl.getCooldown() - 1);
					}
				}
			}
		}, 0L, 20L);
		if(Bukkit.getPluginManager().getPlugin("MVdWPlaceholderAPI") != null){
			PlaceholderAPI.registerPlaceholder(GrandTheftMinecart.getInstance(), "wanted_level", new PlaceholderReplacer(){
				@Override
				public String onPlaceholderReplace(PlaceholderReplaceEvent event){
					Player player = event.getPlayer();
					if(!player.isOnline() || player == null){ return ""; }
					String s = "";
					Players pl = getPlayer(player);
					if(pl == null){ return ""; }
					for(int i = 0; i < pl.getWantedLevel(); i++){
						s = s + "✪";
					}
					return ChatColor.GOLD + s;
				}
			});
		}
	}
	
	public void unlaod(){
		if(Bukkit.getPluginManager().getPlugin("MVdWPlaceholderAPI") != null){
			PlaceholderAPI.getCustomPlaceholders().remove("wanted_level");
		}
	}
	
	public void setWantedLevel(Player player, int i){
		Players pl = getPlayer(player);
		if(i == 0){
			pl.setInfringe(0);
			pl.setCooldown(0);
			pl.setWantedLevel(i);
			for(Entity e : player.getWorld().getEntities()){
				if(e instanceof Zombie){
					Zombie z = (Zombie)e;
					if(z.getTarget() != null && z.getTarget().equals(player)){
						z.setTarget(null);
					}
				}else if(e instanceof Skeleton){
					Skeleton s = (Skeleton)e;
					if(s.getTarget() != null && s.getTarget().equals(player)){
						s.setTarget(null);
					}
				}
			}
		}else if(pl.getWantedLevel() < i){
			pl.setWantedLevel(i);
			pl.setCooldown(StarsType.values()[i - 1].getCooldown());
			spawnEntities(player, i);
		}
	}
	
	public int getWantedLevel(Player player){
		return getPlayer(player).getWantedLevel();
	}
	
	public void spawnEntities(Player player, int i){
		CustomEntityType.getInstance().spawnPolice(player, i);
	}
	
	public Players getPlayer(Player player){
		return players.get(player.getUniqueId().toString());
	}
	
	private void add(Player player){
		Players pl = getPlayer(player);
		if(pl != null)
			return;
		pl = new Players(player);
		players.put(pl.getUUIDString(), pl);
	}
	
	private void remove(Player player){
		Players pl = getPlayer(player);
		if(pl == null)
			return;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		add(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event){
		remove(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		remove(event.getPlayer());
	}
	
	@EventHandler
	public void hurtEntity(EntityDamageByEntityEvent e){
		if(e.getEntity() instanceof Damageable){
			hurtEntity(e.getDamager(), (Damageable)e.getEntity(), e.getDamage());
		}
	}
	
	@EventHandler
	public void onHorseTheft(VehicleEnterEvent e){
		if(e.getVehicle() instanceof Horse && e.getEntered() instanceof Player){
			final Horse h = (Horse)e.getVehicle();
			if(h.getOwner() != null && h.getOwner() != e.getEntered()){
				Player player = (Player)e.getEntered();
				Players pl = getPlayer(player);
				if(pl.getWantedLevel() < Condition.STEALHORSE.getLevel()){
					setWantedLevel(player, Condition.STEALHORSE.getLevel());
					pl.setInfringe(pl.getInfringe() + 1);
				}
			}
		}
	}
	
	public void hurtEntity(Entity edamager, Damageable victim, double damage){
		if(!(edamager instanceof Player)){ return; }
		Player damager = (Player)edamager;
		Players pl = getPlayer(damager);
		Object handle = NMSUtils.getHandle(victim);
		int level = pl.getWantedLevel();
		int set;
		if(victim instanceof Player && damager != victim){
			final Player player = (Player)victim;
			if(((Damageable)player).getHealth() <= damage){
				set = Math.max(pl.getWantedLevel(), Condition.KILLPLAYER.getLevel());
			}else{
				set = Math.max(pl.getWantedLevel(), Condition.ATTACKPLAYER.getLevel());
			}
		}else if(CustomEntityType.getInstance().getEntities().getOfficerClass().equals(handle.getClass()) || CustomEntityType.getInstance().getEntities().getSniperClass().equals(handle.getClass()) || CustomEntityType.getInstance().getEntities().getSwatClass().equals(handle.getClass())){
			if(((Damageable)victim).getHealth() <= damage){
				set = Math.max(pl.getWantedLevel(), Condition.KILLPOLICE.getLevel());
			}else{
				set = Math.max(pl.getWantedLevel(), Condition.ATTACKPOLICE.getLevel());
			}
		}else{
			if(((Damageable)victim).getHealth() <= damage){
				set = Math.max(pl.getWantedLevel(), Condition.KILLENTITY.getLevel());
			}else{
				set = Math.max(pl.getWantedLevel(), Condition.ATTACKENTITY.getLevel());
			}
		}
		if(set > level){
			setWantedLevel(damager, set);
		}
		pl.setInfringe(pl.getInfringe() + 1);
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e){
		setWantedLevel(e.getEntity(), 0);
	}
	
}
