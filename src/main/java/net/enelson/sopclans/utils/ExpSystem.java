package net.enelson.sopclans.utils;

public enum ExpSystem {
	DAMAGE("damage"),
	KILL("kill"),
	COMBINED("COMBINED");

	private String type;

	ExpSystem(String par) {
		this.type = par;
	}

	public String getType() {
		return type;
	}
}
