package me.dablakbandit.grandtheftminecart.nms.v1_11_R1;

import me.dablakbandit.grandtheftminecart.player.PlayerManager;
import me.dablakbandit.grandtheftminecart.player.Players;
import net.minecraft.server.v1_11_R1.Entity;
import net.minecraft.server.v1_11_R1.EntityLiving;
import net.minecraft.server.v1_11_R1.EntityPlayer;

import org.bukkit.entity.Player;

import com.google.common.base.Predicate;

public class EntitySelectorNearestAttackableGangster implements Predicate<Entity> {

	final Predicate<Entity> c;
	final PathfinderGoalNearestAttackableGangster d;
	final int minLevel;

	EntitySelectorNearestAttackableGangster(PathfinderGoalNearestAttackableGangster pathfindergoalnearestattackablegengster, Predicate<Entity> ientityselector, int minLevel) {
		this.d = pathfindergoalnearestattackablegengster;
		this.c = ientityselector;
		this.minLevel = minLevel;
	}

	public boolean apply(Entity entity){
		boolean b = !(entity instanceof EntityPlayer) ? false : (this.c != null && !this.c.apply(entity) ? false : (!checkWantedlevel((EntityPlayer)entity) ? false : this.d.a((EntityLiving) entity,false)));
		return b;
	}

	public boolean checkWantedlevel(EntityPlayer e){
		Players pl = PlayerManager.getInstance().getPlayer((Player)e.getBukkitEntity());
		return pl.getWantedLevel()>=minLevel;	
	}
}
