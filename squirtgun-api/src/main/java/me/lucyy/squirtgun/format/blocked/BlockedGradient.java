package me.lucyy.squirtgun.format.blocked;

import net.kyori.adventure.text.format.TextColor;

/**
 * A blocked gradient - somewhat akin to a striped flag, represents solid and
 * evenly-distributed blocks of colour.
 */
public class BlockedGradient {
	private final TextColor[] cols;
	private final String[] names;

	public BlockedGradient(String[] names, TextColor... cols) {
		this.names = names;
		this.cols = cols;
	}

	public BlockedGradient(String name, TextColor... cols) {
		this.names = new String[]{name};
		this.cols = cols;
	}

	public String[] getNames() {
		return names;
	}

	public TextColor[] getCols() {
		return cols;
	}
}
