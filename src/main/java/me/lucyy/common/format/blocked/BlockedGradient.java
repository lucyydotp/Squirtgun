package me.lucyy.common.format.blocked;

import net.md_5.bungee.api.ChatColor;

public class BlockedGradient {
    private final ChatColor[] cols;
    private final String[] names;
    public BlockedGradient(String[] names, ChatColor... cols) {
        this.names = names;
        this.cols = cols;
    }

    public BlockedGradient(String name, ChatColor... cols) {
        this.names = new String[]{name};
        this.cols = cols;
    }

    public String[] getNames() {
        return names;
    }

    public ChatColor[] getCols() {
        return cols;
    }
}
