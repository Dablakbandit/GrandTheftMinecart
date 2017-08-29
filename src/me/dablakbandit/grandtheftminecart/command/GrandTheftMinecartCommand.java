package me.dablakbandit.grandtheftminecart.command;

import me.dablakbandit.dabcore.command.AbstractCommand;
import me.dablakbandit.grandtheftminecart.GrandTheftMinecart;
import me.dablakbandit.grandtheftminecart.player.PlayerManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;

public class GrandTheftMinecartCommand extends AbstractCommand{

	private static GrandTheftMinecartCommand command = new GrandTheftMinecartCommand(GrandTheftMinecart.getInstance().getCommand());

	public static GrandTheftMinecartCommand getInstance(){
		return command;
	}

	private GrandTheftMinecartCommand(String command){
		super(command);
		register();
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args){
		if(!s.hasPermission("gtm.admin"))return false;
		if(args.length==0){
			s.sendMessage(ChatColor.AQUA + "-=-=-=-=-=[GTM]-=-=-=-=-");
			if(s.hasPermission("gtm.killall"))s.sendMessage("/gtm killall");
			if(s.hasPermission("gtm.swl"))s.sendMessage("/gtm swl <player> <level>");
			return true;
		}else{
			switch(args[0].toLowerCase()){
			case "swl":{
				if(!s.hasPermission("gtm.swl"))return false;
				if(args.length!=3){
					s.sendMessage(ChatColor.AQUA + "-=-=-=-=-=[GTM SWL]-=-=-=-=-");
					s.sendMessage("/gtm swl <player> <level>");
					return false;
				}
				Player p  = Bukkit.getServer().getPlayer(args[1]);
				if(p==null){
					s.sendMessage("Unknown player " + args[1]);
					return false;
				}
				int i;
				try{
					i = Integer.valueOf(args[2]);
					if(i<0||i>5){
						s.sendMessage("Choose a number 0-5");
						return false;
					}
				}catch(NumberFormatException e){
					s.sendMessage("Choose a number 0-5");
					return false;
				}
				PlayerManager.getInstance().setWantedLevel(p, i);
				s.sendMessage(ChatColor.GREEN + args[1] + " wanted level set to "+ i);
				return true;
			}
			case "killall":{
				if(!s.hasPermission("gtm.killall"))return false;
				Player p = (Player)s;
				int i = 0;
				for(Entity e : p.getWorld().getEntities()){
						if(e instanceof Zombie||e instanceof Skeleton||e instanceof PigZombie){
							e.remove();
							i++;
						}
				}
				s.sendMessage(i + " policemembers were killed");
				return true;
			}
			default:{
				return false;
			}
			}
		}
	}

}
