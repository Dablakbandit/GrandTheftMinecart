package me.dablakbandit.grandtheftminecart.nms;

import java.util.Arrays;
import java.util.List;

import me.dablakbandit.dabcore.utils.NMSUtils;

public class IEntities {

	private List<IEntity> entities = get();
	
	private List<IEntity> get(){
		try{
			return Arrays.asList(new IEntity[]{
					new IEntity(Class.forName("me.dablakbandit.grandtheftminecart.nms." + NMSUtils.getVersion() + "PoliceOfficer"), NMSUtils.getNMSClass("EntityZombie"), "Zombie", 54),
					new IEntity(Class.forName("me.dablakbandit.grandtheftminecart.nms." + NMSUtils.getVersion() + "Sniper"), NMSUtils.getNMSClass("EntitySkeleton"), "Skeleton", 51),
					new IEntity(Class.forName("me.dablakbandit.grandtheftminecart.nms." + NMSUtils.getVersion() + "Swat"), NMSUtils.getNMSClass("EntityPigZombie"), "PigZombie", 57)
			});
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public List<IEntity> getEntities(){
		return entities;
	}

	public Class<?> getOfficerClass(){
		return entities.get(0).getCustomClass();
	}
	
	public Class<?> getSniperClass(){
		return entities.get(1).getCustomClass();
	}
	
	public Class<?> getSwatClass(){
		return entities.get(2).getCustomClass();
	}
}
