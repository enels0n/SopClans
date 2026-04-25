package net.enelson.sopclans.placeholders;

public enum Placeholder {

	CLAN("clan"),
	TOP("top");

	private String placeholder;

	Placeholder(String placeholder) {
		this.placeholder = placeholder;
	}

	public String getTextParameter() {
		return placeholder;
	}
}
