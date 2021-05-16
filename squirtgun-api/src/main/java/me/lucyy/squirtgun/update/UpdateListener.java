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

package me.lucyy.squirtgun.update;

import me.lucyy.squirtgun.platform.EventListener;
import me.lucyy.squirtgun.platform.Platform;
import me.lucyy.squirtgun.platform.SquirtgunPlayer;
import java.util.UUID;

class UpdateListener extends EventListener {
	private final UpdateChecker checker;
	private final Platform platform;

	public UpdateListener(UpdateChecker checker, Platform platform) {
		this.checker = checker;
		this.platform = platform;
	}

	@Override
	public void onPlayerJoin(UUID uuid) {
		super.onPlayerJoin(uuid);
		SquirtgunPlayer player = platform.getPlayer(uuid);
		if (checker.checkDataForUpdate() && platform.getPlayer(uuid).hasPermission(checker.getListenerPermission()))
			player.sendMessage(checker.getUpdateMessage());
	}
}
