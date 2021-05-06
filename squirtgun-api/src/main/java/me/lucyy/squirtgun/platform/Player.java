package me.lucyy.squirtgun.platform;

import net.kyori.adventure.audience.Audience;

public interface Player extends Audience {
	boolean hasPermission(String permission);
}
