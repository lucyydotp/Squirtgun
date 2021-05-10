package me.lucyy.squirtgun.platform;

import java.util.UUID;

public interface Player extends Commander {
	UUID getUuid();

	String getUsername();

	boolean isOnline();

	boolean hasPermission(String permission);

	Gamemode getGamemode();

	void setGamemode(Gamemode mode);
}
