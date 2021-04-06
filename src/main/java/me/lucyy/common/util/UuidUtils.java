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

package me.lucyy.common.util;

import java.util.UUID;

/**
 * Various utilities for formatting UUIDs.
 */
public class UuidUtils {
	/**

	 * Converts a UUID to a string, removing hyphens.
	 *
	 * @param uuid the UUID to convert to a string
	 * @return the string form of a UUID with no hyphens.
	 */
	public static String toString(UUID uuid) {
		return uuid.toString().replace("-", "");
	}

	/**
	 * Converts a UUID, either with or without dashes, to a string. Hyphens are optional.
	 *
	 * @param name the string to parse
	 * @return the parsed UUID
	 * @throws IllegalArgumentException if the provided string is incorrectly formatted
	 */
	public static UUID fromString(String name) {
		return UUID.fromString(name.replaceFirst(
				"(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
				"$1-$2-$3-$4-$5")
		);
	}
}