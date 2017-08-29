package me.dablakbandit.grandtheftminecart.nms.v1_8_R3;

import java.util.Collections;
import java.util.List;

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.PathfinderGoalTarget;

import org.bukkit.event.entity.EntityTargetEvent;

import com.google.common.base.Predicate;

@SuppressWarnings("unchecked")
public class PathfinderGoalNearestAttackableGangster extends PathfinderGoalTarget {

	private final Class<? extends Entity> a;
    private final int b;
    private final DistanceComparator e1;
    private final Predicate<Entity> f;
    private EntityLiving g;

    public PathfinderGoalNearestAttackableGangster(EntityCreature entitycreature, Class<?> oclass, int i, boolean flag, int minLevel) {
        this(entitycreature, oclass, i, flag, false, minLevel);
    }

    public PathfinderGoalNearestAttackableGangster(EntityCreature entitycreature, Class<?> oclass, int i, boolean flag, boolean flag1, int minLevel) {
        this(entitycreature, oclass, i, flag, flag1, (Predicate<Entity>) null, minLevel);
    }

	public PathfinderGoalNearestAttackableGangster(EntityCreature entitycreature, Class<?> oclass, int i, boolean flag, boolean flag1, Predicate<Entity> ientityselector, int minLevel) {
        super(entitycreature,flag, flag1);
        this.a = (Class<? extends Entity>) oclass;
        this.b = i;
        this.e1 = new DistanceComparator(entitycreature);
        this.a(1);
        this.f = new EntitySelectorNearestAttackableGangster(this, ientityselector, minLevel);
    }

	public boolean a(){
        if(this.b>0&&this.e.bc().nextInt(this.b)!=0){
        	this.g = null;
            return true;
        }else{
            double d0 = this.f();
            List<Entity> list = this.e.world.a(this.a, this.e.getBoundingBox().grow(d0, 4.0D, d0), this.f);
            Collections.sort(list, this.e1);
            if(list.isEmpty()){
            	this.g = null;
                return true;
            }else{
                this.g = (EntityLiving)list.get(0);
                return true;
            }
        }
    }

    public void c(){
    	try{
        this.e.setGoalTarget(this.g, EntityTargetEvent.TargetReason.FORGOT_TARGET, true);
    	}catch(Exception e){return;}
        super.c();
    }
    
    public boolean a(EntityLiving e, boolean flag){
    	return super.a(e, flag);
    }
}