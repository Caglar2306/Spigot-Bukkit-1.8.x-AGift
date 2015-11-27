package com.caglardev.agift.objects;

import org.bukkit.Material;

public class Gift {

	private Material material;
	private int stack;
	private String displayName;
	
	public Gift(Material material, int stack, String displayName) {
		this.material = material;
		this.stack = stack;
		this.displayName = displayName;
	}
	
	public Gift(Material material, int stack) {
		this.material = material;
		this.stack = stack;
		this.displayName = null;
	}
	
	public void setMaterial(Material material) {
		this.material = material;
	}
	
	public void setStack(int stack) {
		this.stack = stack;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public Material getMaterial() {
		return this.material;
	}
	
	public int getStack() {
		if(this.stack > this.getMaterial().getMaxStackSize()) {
			this.stack = this.getMaterial().getMaxStackSize();
		}
		
		return this.stack;
	}
	
	public String getDisplayName() {
		return this.displayName;
	}
	
}
