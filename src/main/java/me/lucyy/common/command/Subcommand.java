/*
 * Copyright (C) 2021 Lucy Poulton https://lucyy.me
 * This file is part of ProFiles.
 *
 * ProFiles is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ProFiles is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ProFiles.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.lucyy.common.command;

import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * A subcommand.
 */
public interface Subcommand {
	/**
	 * Gets the name of the subcommand. Must not contain spaces.
	 */
	String getName();

	/**
	 * Get the description of the subcommand. This will be shown to the user in help commands,
	 */
	String getDescription();

	/**
	 * Gets the usage string for this subcommand.
	 */
	String getUsage();

	/**
	 * Gets the permission needed to execute this subcommand
	 * @return the permission, or null if no permission is needed
	 */
	String getPermission();

	/**
	 * Execute the command.
	 * @param sender The player to run the command as.
	 * @param target The player to act against. Note that this is different from the sender when, for example, the
	 *               /pronouns sudo command is executed.
	 * @param args The arguments.
	 * @return true if the command is syntactically correct; false if not (this will print the {@link #getUsage()}
	 * message)
	 */
	boolean execute(CommandSender sender, CommandSender target, String[] args);

	/**
	 * Tab-complete this command.
	 * @param args the command's arguments so far.
	 * @return a string list of potential options
	 */
	default List<String> tabComplete(String[] args) {
		return null;
	}
}
