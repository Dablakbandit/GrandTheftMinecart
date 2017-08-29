package me.dablakbandit.grandtheftminecart.nms;

import java.util.HashMap;
import java.util.Map;

import me.dablakbandit.grandtheftminecart.GrandTheftMinecart;

import org.bukkit.configuration.Configuration;

public enum StarsType {
	ONESTAR(2, 0, 0, 40), TWOSTAR(4, 0, 0, 60), THREESTARS(6, 3, 0, 80), FOURSTARS(0, 2, 4, 100), FIVESTARS(0, 4, 8, 120);

	private int officer, sniper, swat, cooldown;

	StarsType(int officers, int snipers, int swats, int cooldown){
		Configuration config = GrandTheftMinecart.getInstance().getConfig();
		if(config.isSet("Spawn." + name() + ".Officer")){
			this.officer = config.getInt("Spawn." + name() + ".Officer");
		}else{
			config.set("Spawn." + name() + ".Officer", officers);
			this.officer = officers;
		}
		if(config.isSet("Spawn." + name() + ".Snipers")){
			this.sniper = config.getInt("Spawn." + name() + ".Sniper");
		}else{
			config.set("Spawn." + name() + ".Sniper", snipers);
			this.sniper = snipers;
		}
		if(config.isSet("Spawn." + name() + ".Swat")){
			this.swat = config.getInt("Spawn." + name() + ".Swat");
		}else{
			config.set("Spawn." + name() + ".Swat", swats);
			this.swat = swats;
		}
		if(config.isSet("Cooldown." + name())){
			this.cooldown = config.getInt("Cooldown." + name());
		}else{
			config.set("Cooldown." + name(), cooldown);
			this.cooldown = cooldown;
		}
	}
	
	public Map<IEntity, Integer> getSpawns(){
		Map<IEntity, Integer> spawns = new HashMap<IEntity, Integer>();
		spawns.put(CustomEntityType.getInstance().getEntities().getEntities().get(0), officer);
		spawns.put(CustomEntityType.getInstance().getEntities().getEntities().get(1), sniper);
		spawns.put(CustomEntityType.getInstance().getEntities().getEntities().get(2), swat);
		return spawns;
	}
	
	public int getCooldown(){
		return cooldown;
	}
	
	public int getOfficer(){
		return officer;
	}
	
	public int getSniper(){
		return sniper;
	}
	
	public int getSwat(){
		return swat;
	}
}
