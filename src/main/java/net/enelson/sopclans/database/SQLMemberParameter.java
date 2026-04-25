package net.enelson.sopclans.database;

public enum SQLMemberParameter {

	RANK("rank"),
	EXP("exp"),
	ISACTIVE("isActive");

	private String par;

	SQLMemberParameter(String par) {
		this.par = par;
	}

	public String getTextParameter() {
		return par;
	}
}
