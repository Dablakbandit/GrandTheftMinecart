package me.dablakbandit.grandtheftminecart;

import java.lang.reflect.Field;
import java.util.Arrays;

import me.dablakbandit.dabcore.configuration.Configuration;
import me.dablakbandit.grandtheftminecart.LanguageConfiguration.LanguageMessage;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("deprecation")
public class ItemConfiguration extends Configuration{

	private static ItemConfiguration config;
	
	public static Items OFFICER_HELMET = new Items(Material.AIR, 0, 0);
	public static Items OFFICER_CHESTPLATE = new Items(Material.LEATHER_CHESTPLATE, DyeColor.BLUE.getDyeData(), 1);
	public static Items OFFICER_LEGGINGS = new Items(Material.LEATHER_LEGGINGS, DyeColor.BLUE.getDyeData(), 1);
	public static Items OFFICER_BOOTS = new Items(Material.LEATHER_BOOTS, DyeColor.BLUE.getDyeData(), 1);
	public static Items OFFICER_WEAPON = new Items(Material.AIR, 0, 0);
	
	public static Items SNIPER_HELMET = new Items(Material.AIR, 0, 0);
	public static Items SNIPER_CHESTPLATE = new Items(Material.LEATHER_CHESTPLATE, DyeColor.BLACK.getDyeData(), 1);
	public static Items SNIPER_LEGGINGS = new Items(Material.LEATHER_LEGGINGS, DyeColor.BLACK.getDyeData(), 1);
	public static Items SNIPER_BOOTS = new Items(Material.LEATHER_BOOTS, DyeColor.BLACK.getDyeData(), 1);
	public static Items SNIPER_WEAPON = new Items(Material.BOW, 0, 1);
	
	public static Items SWAT_HELMET = new Items(Material.LEATHER_HELMET, DyeColor.BLACK.getDyeData(), 1);
	public static Items SWAT_CHESTPLATE = new Items(Material.LEATHER_CHESTPLATE, DyeColor.BLACK.getDyeData(), 1);
	public static Items SWAT_LEGGINGS = new Items(Material.LEATHER_LEGGINGS, DyeColor.BLACK.getDyeData(), 1);
	public static Items SWAT_BOOTS = new Items(Material.LEATHER_BOOTS, DyeColor.BLACK.getDyeData(), 1);
	public static Items SWAT_WEAPON = new Items(Material.IRON_SWORD, 0, 1);
	
	private ItemConfiguration(JavaPlugin plugin, String filename) {
		super(plugin, filename);
	}

	public static void setup(JavaPlugin plugin, String filename){
		config = new ItemConfiguration(plugin, filename);
		load();
	}
	
	public static void load(){
		config.ReloadConfig();
		try{
			for(Field f : ItemConfiguration.class.getDeclaredFields()){
				if(f.getType().equals(Items.class)){
					((Items)f.get(null)).get(f.getName().replaceFirst("_", "."));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void reload(){
		load();
	}

	public static class Items{
		private String material;
		private int durability, amount;

		private Items(Material material, int durability, int amount){
			this.material = material.name();
			this.durability = durability;
			this.amount = amount;
		}

		private void get(String path){
			if(config.GetConfig().isSet(path)){
				Material tm = Material.valueOf(config.GetConfig().getString(path + ".Material").toUpperCase());
				if(tm==null){
					System.out.print("Error loading item from items.yml '" + path + "' unknown material");
					return;
				}
				int i;
				String s = "";
				try{
					s = "" + config.GetConfig().getInt(path + ".Durability");
					i = Integer.parseInt("" + config.GetConfig().getInt(path + ".Durability"));
				}catch(Exception e){
					System.out.print("Error loading item from items.yml '" + path + "' cannot parse " + s);
					return;
				}
				int ii;
				try{
					s = "" + config.GetConfig().getInt(path + ".Amount");
					ii = Integer.parseInt("" + config.GetConfig().getInt(path + ".Amount"));
				}catch(Exception e){
					System.out.print("Error loading item from items.yml '" + path + "' cannot parse " + s);
					return;
				}
				this.material = tm.name();
				this.durability = i;
				this.amount = ii;
			}else{
				config.GetConfig().set(path + ".Material", material);
				config.GetConfig().set(path + ".Durability", durability);
				config.GetConfig().set(path + ".Amount", amount);
				config.SaveConfig();
			}
		}

		public Material getMaterial(){
			return Material.valueOf(material);
		}

		public int getDurability(){
			return durability;
		}
		
		public int getAmount(){
			return amount;
		}
		
		public ItemStack getItemStack(){
			Material m = getMaterial();
			if(m!=Material.AIR)return new ItemStack(getMaterial(), amount, (short)durability);
			else return null;
		}
		
		public ItemStack getItemStack(LanguageMessage display){
			return getItemStack(display.getMessage());
		}
		
		public ItemStack getItemStack(LanguageMessage display, LanguageMessage... lores){
			String[] slores = new String[lores.length];
			int i = 0;
			for(LanguageMessage lm : lores){
				slores[i++] = lm.getMessage();
			}
			return getItemStack(display != null ? display.getMessage() : null, slores);
		}
		
		public ItemStack getItemStack(String display){
			ItemStack is = getItemStack();
			if(is==null)return is;
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(display);
			is.setItemMeta(im);
			return is;
		}
		
		public ItemStack getItemStack(String display, String... lores){
			ItemStack is = getItemStack();
			if(is==null)return is;
			ItemMeta im = is.getItemMeta();
			if(display!=null)im.setDisplayName(display);
			im.setLore(Arrays.asList(lores));
			is.setItemMeta(im);
			return is;
		}
	}
}
