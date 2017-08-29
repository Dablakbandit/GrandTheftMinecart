package me.dablakbandit.grandtheftminecart.nms.v1_8_R3;

import java.util.Comparator;

import net.minecraft.server.v1_8_R3.Entity;

public class DistanceComparator implements Comparator<Entity>{
	private final Entity a;

	public DistanceComparator(Entity paramEntity){
		this.a = paramEntity;
	}

	public int compare(Entity paramEntity1, Entity paramEntity2){
		double d1 = this.a.h(paramEntity1);
		double d2 = this.a.h(paramEntity2);
		if (d1 < d2) {
			return -1;
		}
		if (d1 > d2) {
			return 1;
		}
		return 0;
	}
}
