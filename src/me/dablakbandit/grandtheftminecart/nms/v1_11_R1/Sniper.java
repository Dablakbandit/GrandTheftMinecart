package me.dablakbandit.grandtheftminecart.nms.v1_11_R1;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_11_R1.util.UnsafeList;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import me.dablakbandit.grandtheftminecart.GrandTheftMinecart;
import me.dablakbandit.grandtheftminecart.ItemConfiguration;
import me.dablakbandit.grandtheftminecart.LanguageConfiguration;
import net.minecraft.server.v1_11_R1.EntityHuman;
import net.minecraft.server.v1_11_R1.EntitySkeleton;
import net.minecraft.server.v1_11_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_11_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_11_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_11_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_11_R1.PathfinderGoalMoveIndoors;
import net.minecraft.server.v1_11_R1.PathfinderGoalOpenDoor;
import net.minecraft.server.v1_11_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_11_R1.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_11_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_11_R1.World;

public class Sniper extends EntitySkeleton{
	
	public static ItemStack[] equip =
		new ItemStack[]{ ItemConfiguration.SNIPER_HELMET.getItemStack(), ItemConfiguration.SNIPER_CHESTPLATE.getItemStack(), ItemConfiguration.SNIPER_LEGGINGS.getItemStack(), ItemConfiguration.SNIPER_BOOTS.getItemStack(), ItemConfiguration.SNIPER_WEAPON.getItemStack() };
	
	public Sniper(World world){
		super(world);
		try{
			Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
			bField.setAccessible(true);
			Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
			cField.setAccessible(true);
			bField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
			bField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
			cField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
			cField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
			this.setCustomName(LanguageConfiguration.NAMES_SNIPER.getMessage());
			this.setCustomNameVisible(GrandTheftMinecart.getInstance().getVisible());
		}catch(Exception exc){
			exc.printStackTrace();
		}
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(1, new PathfinderGoalMoveIndoors(this));
		this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
		this.goalSelector.a(5, new PathfinderGoalOpenDoor(this, true));
		this.goalSelector.a(5, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, 1.0D));
		this.goalSelector.a(7, new PathfinderGoalMeleeAttack(this, 1.0D, false));
		this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
		this.targetSelector.a(2, new PathfinderGoalNearestAttackableGangster(this, EntityHuman.class, 0, true, 3));
		EntityEquipment e = ((Skeleton)this.getBukkitEntity()).getEquipment();
		e.setHelmet(equip[0]);
		e.setChestplate(equip[1]);
		e.setLeggings(equip[2]);
		e.setBoots(equip[3]);
		if(world != null && !world.isClientSide){
			e.setItemInHand(equip[4]);
		}
	}
}
