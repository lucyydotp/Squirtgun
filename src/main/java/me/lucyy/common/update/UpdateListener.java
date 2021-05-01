/*
 * Copyright (C) 2021 Lucy Poulton https://lucyy.me
 * This file is part of LucyCommonLib.
 *
 * LucyCommonLib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LucyCommonLib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LucyCommonLib.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.lucyy.common.update;

import me.lucyy.common.format.Platform;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

class UpdateListener implements Listener {
	private final UpdateChecker checker;

	public UpdateListener(UpdateChecker checker) {
		this.checker = checker;
	}

	@EventHandler
	public void on(PlayerJoinEvent e) {
		if (checker.checkDataForUpdate() && e.getPlayer().hasPermission(checker.getListenerPermission()))
			Platform.send(e.getPlayer(), checker.getUpdateMessage());
	}
}
