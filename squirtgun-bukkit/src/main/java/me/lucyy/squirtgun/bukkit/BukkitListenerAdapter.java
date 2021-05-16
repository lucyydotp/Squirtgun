package me.lucyy.squirtgun.bukkit;

import me.lucyy.squirtgun.platform.EventListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BukkitListenerAdapter implements Listener {
	private final List<EventListener> listeners = new ArrayList<>();

	public void addListener(EventListener listener) {
		listeners.add(listener);
	}

	public void removeListener(EventListener listener) {
		listeners.remove(listener);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		UUID uuid = e.getPlayer().getUniqueId();
		for (EventListener listener : listeners) {
			listener.onPlayerJoin(uuid);
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerQuitEvent e) {
		UUID uuid = e.getPlayer().getUniqueId();
		for (EventListener listener : listeners) {
			listener.onPlayerJoin(uuid);
		}
	}
}
