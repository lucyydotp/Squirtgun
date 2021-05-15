package me.lucyy.squirtgun.platform;

import net.kyori.adventure.audience.Audience;

import java.util.UUID;

public interface Player extends PermissionHolder, Audience {
	UUID getUuid();

	String getUsername();

	boolean isOnline();

	boolean hasPermission(String permission);

	Gamemode getGamemode();

	void setGamemode(Gamemode mode);
}
