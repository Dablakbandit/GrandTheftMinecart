package me.dablakbandit.grandtheftminecart.player;

import java.util.UUID;

import org.bukkit.entity.Player;

public class Players {

	private String uuid;
	private int wanted, cooldown, infringe;

	public Players(Player player){
		uuid = player.getUniqueId().toString();
	}

	public String getUUIDString(){
		return uuid;
	}

	public UUID getUUID(){
		return UUID.fromString(uuid);
	}

	public int getWantedLevel(){
		return wanted;
	}

	public void setWantedLevel(int i){
		wanted = i;
	}

	public int getCooldown(){
		return cooldown;
	}

	public void setCooldown(int i){
		cooldown = i;
	}

	public int getInfringe(){
		return infringe;
	}

	public void setInfringe(int i){
		infringe = i;
		checkWanted();
	}

	protected void checkWanted(){
		if(infringe>=50){
			if(wanted<5)wanted = 5;
		}else if(infringe>=20){
			if(wanted<4)wanted = 4;
		}
	}
}
