package me.dablakbandit.grandtheftminecart.nms;

public class IEntity {

	private Class<?> custom, nms;
	private String name;
	private int id;
	
	public IEntity(Class<?> custom, Class<?> nms, String name, int id){
		this.custom = custom;
		this.nms = nms;
		this.name = name;
		this.id = id;
	}
	
	
	public Class<?> getCustomClass(){
		return custom;
	}
	
	public Class<?> getNMSClass(){
		return nms;
	}
	
	public String getName(){
		return name;
	}
	
	public int getID(){
		return id;
	}
}
