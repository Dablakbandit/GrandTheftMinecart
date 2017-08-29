package me.dablakbandit.grandtheftminecart;

import java.lang.reflect.Field;

import me.dablakbandit.dabcore.configuration.Configuration;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class LanguageConfiguration extends Configuration{

	private static LanguageConfiguration config;
	
	public static LanguageMessage NAMES_POLICE_OFFICER = new LanguageMessage("&" + ChatColor.BLUE.getChar() + "Police Officer");
	public static LanguageMessage NAMES_SNIPER = new LanguageMessage("&" + ChatColor.GRAY.getChar() + "Sniper");
	public static LanguageMessage NAMES_SWAT = new LanguageMessage("&" + ChatColor.DARK_GRAY.getChar() + "SWAT");
	
	private LanguageConfiguration(JavaPlugin plugin, String filename) {
		super(plugin, filename);
	}

	public static void setup(JavaPlugin plugin, String filename){
		config = new LanguageConfiguration(plugin, filename);
		load();
	}

	public static void load(){
		try{
			for(Field f : LanguageConfiguration.class.getDeclaredFields()){
				if(f.getType().equals(LanguageMessage.class)){
					((LanguageMessage)f.get(null)).checkOld();
				}
			}
			for(Field f : LanguageConfiguration.class.getDeclaredFields()){
				if(f.getType().equals(LanguageMessage.class)){
					((LanguageMessage)f.get(null)).get(f.getName().replaceFirst("_", "."));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void reload(){
		config.ReloadConfig();
		load();
	}

	public static class LanguageMessage{

		private String old, def, get;

		private LanguageMessage(String old, String def){
			this.old = old;
			this.def = def;
		}

		private LanguageMessage(String def){
			this.def = def;
		}

		private void get(String path){
			if(config.GetConfig().isSet(path)){
				get = config.GetConfig().getString(path);
			}else{
				config.GetConfig().set(path, def);
				config.SaveConfig();
				get = def;
			}
			get = ChatColor.translateAlternateColorCodes('&', get);
		}

		private void checkOld(){
			if(old!=null&&config.GetConfig().isSet(old)){
				String s = config.GetConfig().getString(old);
				if(!s.startsWith("MemorySection")){
					def = s;
					config.GetConfig().set(old, null);
				}
			}
		}

		public String getMessage(){
			return get;
		}
	}
}
