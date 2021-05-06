package me.lucyy.squirtgun.platform;

public enum AuthMode {
	OFFLINE("-c"),
	ONLINE(""),
	BUNGEE("-b");

	private final String value;

	AuthMode(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
