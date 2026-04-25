package net.enelson.sopclans.database;

public enum SQLClanParameter {

	NAME("name"),
	TAG("tag"),
	LEVEL("level"),
	EXP("exp"),
	SLOTS("slots"),
	ISACTIVE("isActive"),
	ISPVPENABLED("isPvpEnabled"),
	CLANHOME("clanhome");

	private String par;

	SQLClanParameter(String par) {
		this.par = par;
	}

	public String getTextParameter() {
		return par;
	}
}
