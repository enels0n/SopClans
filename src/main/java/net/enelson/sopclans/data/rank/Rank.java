package net.enelson.sopclans.data.rank;

import java.util.HashMap;

public class Rank {
	private String name;
	private String displayName;
	private HashMap<String,Boolean> permissions;
	private double promotePrice;
	private double demotePrice;
	private boolean leader;
	
	Rank(String name, String displayName, HashMap<String,Boolean> permissions, double promotePrice, double demotePrice) {
		this.name = name;
		this.displayName = displayName;
		this.permissions = permissions;
		this.promotePrice = promotePrice;
		this.demotePrice = demotePrice;
		this.leader = (name.equals("leader")) ? true : false;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDisplayName() {
		return this.displayName;
	}
	
	public Boolean getPermission(String permission) {
		if(this.name.equals("leader"))
			return true;
		if(this.permissions.containsKey(permission))
			return this.permissions.get(permission);
		return false;
	}
	
	public double getPromotePrice() {
		return this.promotePrice;
	}
	
	public double getDemotePrice() {
		return this.demotePrice;
	}
	
	public boolean isLeader() {
		return this.leader;
	}
}
