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
	 * Returns the string form of a UUID with no dashes.
	 */
	public static String toString(UUID uuid) {
		return uuid.toString().replace("-", "");
	}

	/**
	 * Converts a UUID, either with or without dashes, to a string.
	 */
	public static UUID fromString(String string) {
		return UUID.fromString(string.replaceFirst(
				"(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
				"$1-$2-$3-$4-$5")
		);
	}
}