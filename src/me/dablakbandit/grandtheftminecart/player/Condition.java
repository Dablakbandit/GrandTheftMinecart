package me.dablakbandit.grandtheftminecart.player;

import me.dablakbandit.grandtheftminecart.GrandTheftMinecart;

import org.bukkit.configuration.Configuration;

public enum Condition {
	ATTACKENTITY(0), KILLENTITY(0), ATTACKPLAYER(0), KILLPLAYER(1), STEALHORSE(1), ATTACKPOLICE(2), KILLPOLICE(3);
	
	private int level;
	
	Condition(int level){
		Configuration config = GrandTheftMinecart.getInstance().getConfig();
		if(config.isSet("Condition." + name())){
			this.level = config.getInt("Condition." + name());
		}else{
			config.set("Condition." + name(), level);
			this.level = level;
		}
		
	}
	
	public int getLevel(){
		return level;
	}
}
