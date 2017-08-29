package me.dablakbandit.grandtheftminecart.nms.v1_11_R1;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_11_R1.util.UnsafeList;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import me.dablakbandit.grandtheftminecart.GrandTheftMinecart;
import me.dablakbandit.grandtheftminecart.ItemConfiguration;
import me.dablakbandit.grandtheftminecart.LanguageConfiguration;
import net.minecraft.server.v1_11_R1.EntityHuman;
import net.minecraft.server.v1_11_R1.EntityZombie;
import net.minecraft.server.v1_11_R1.GenericAttributes;
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

public class PoliceOfficer extends EntityZombie{
	
	public static ItemStack[] equip =
		new ItemStack[]{ ItemConfiguration.OFFICER_HELMET.getItemStack(), ItemConfiguration.OFFICER_CHESTPLATE.getItemStack(), ItemConfiguration.OFFICER_LEGGINGS.getItemStack(), ItemConfiguration.OFFICER_BOOTS.getItemStack(), ItemConfiguration.OFFICER_WEAPON.getItemStack() };
	
	public PoliceOfficer(World world){
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
			this.setCustomName(LanguageConfiguration.NAMES_POLICE_OFFICER.getMessage());
			this.setCustomNameVisible(GrandTheftMinecart.getInstance().getVisible());
		}catch(Exception exc){
			exc.printStackTrace();
		}
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
		this.goalSelector.a(3, new PathfinderGoalMoveIndoors(this));
		this.goalSelector.a(5, new PathfinderGoalOpenDoor(this, true));
		this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
		this.goalSelector.a(7, new PathfinderGoalMeleeAttack(this, 1.0D, false));
		this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
		this.targetSelector.a(2, new PathfinderGoalNearestAttackableGangster(this, EntityHuman.class, 0, true, 1));
		setBaby(false);
		EntityEquipment e = ((Zombie)this.getBukkitEntity()).getEquipment();
		e.setHelmet(equip[0]);
		e.setChestplate(equip[1]);
		e.setLeggings(equip[2]);
		e.setBoots(equip[3]);
		e.setItemInHand(equip[4]);
	}
	
	@Override
	protected void initAttributes(){
		super.initAttributes();
		this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(3.0D);
	}
	
}