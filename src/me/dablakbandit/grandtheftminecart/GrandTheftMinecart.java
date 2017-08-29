package me.dablakbandit.grandtheftminecart;

import me.dablakbandit.grandtheftminecart.command.GrandTheftMinecartCommand;
import me.dablakbandit.grandtheftminecart.nms.CustomEntityType;
import me.dablakbandit.grandtheftminecart.nms.StarsType;
import me.dablakbandit.grandtheftminecart.player.PlayerManager;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class GrandTheftMinecart extends JavaPlugin{

	private static GrandTheftMinecart main;

	private String command;
	private boolean visible;

	public static GrandTheftMinecart getInstance(){
		return main;
	}

	public void onLoad(){
		main = this;
		saveDefaultConfig();
		CustomEntityType.getInstance().registerEntities();
		LanguageConfiguration.setup(this, "language.yml");
		ItemConfiguration.setup(this, "items.yml");
	}

	public void onEnable(){
		command = getStringFromConfig("Command", "gtm");
		visible = getBooleanFromConfig("MobNameVisible", false);
		StarsType.ONESTAR.getOfficer();
		PlayerManager.getInstance();
		GrandTheftMinecartCommand.getInstance();
		Bukkit.getServer().getPluginManager().registerEvents(new Listener(){
			@EventHandler
			public void onCombust(EntityCombustEvent event){
				Entity e = event.getEntity();
				if(e instanceof Zombie||e instanceof Skeleton||e instanceof PigZombie){
					event.setCancelled(true);
				}
			}
		}, this);
	}

	public String getCommand(){
		return command;
	}
	
	public boolean getVisible(){
		return visible;
	}

	public void onDisable(){
		despawnCustomEntitys();
		CustomEntityType.getInstance().unregisterEntities();
		PlayerManager.getInstance().unlaod();
	}

	private void despawnCustomEntitys(){
		for(World w : getServer().getWorlds()){
			for(Entity e : w.getEntities()){
				if(e instanceof Zombie||e instanceof Skeleton||e instanceof PigZombie){
					e.remove();
				}
			}
		}
	}

	private String getStringFromConfig(String s, String def){
		if(getConfig().isSet(s)){
			return getConfig().getString(s);
		}else{
			getConfig().set(s, def);
			saveConfig();
			return def;
		}
	}
	
	public boolean getBooleanFromConfig(String s, boolean def){
		if(getConfig().isSet(s)){
			return getConfig().getBoolean(s);
		}else{
			getConfig().set(s, def);
			saveConfig();
			return def;
		}
	}

}
