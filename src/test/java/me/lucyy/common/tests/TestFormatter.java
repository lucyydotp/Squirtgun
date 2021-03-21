package me.lucyy.common.tests;

import me.lucyy.common.command.FormatProvider;
import net.md_5.bungee.api.ChatColor;

public class TestFormatter implements FormatProvider {
    @Override
    public String getMainColour() {
        return null;
    }

    @Override
    public String formatMain(String input) {
        return ChatColor.WHITE + input;
    }

    @Override
    public String formatMain(String input, String formatters) {
        return ChatColor.WHITE + formatters + input;
    }

    @Override
    public String getAccentColour() {
        return null;
    }

    @Override
    public String formatAccent(String input) {
        return ChatColor.YELLOW + input;
    }

    @Override
    public String formatAccent(String input, String formatters) {
        return ChatColor.YELLOW + formatters + input;
    }

    @Override
    public String getPrefix() {
        return "TEST";
    }
}
