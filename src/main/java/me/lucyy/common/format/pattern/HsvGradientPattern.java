package me.lucyy.common.format.pattern;

import me.lucyy.common.format.TextFormatter;
import me.lucyy.common.format.pattern.FormatPattern;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.util.HSVLike;
import net.md_5.bungee.api.ChatColor;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HsvGradientPattern implements FormatPattern {
    final Pattern pattern = Pattern.compile("\\{hsv:([A-Fa-f0-9]{6}):?([klmno]+)?>}(.*)\\{([A-Fa-f0-9]{2})<}");

    public static Component fade(int hue1, int hue2, int sat, int val, String text, String formats) {
        Component component = Component.empty();

        StringBuilder output = new StringBuilder();

        int[] hues = TextFormatter.fade(text.length(), hue1, hue2);

        if (formats != null) {
            for (char c : formats.toCharArray()) {
                //noinspection ConstantConditions - the char is checked in regex
                component = component.decorate(LegacyComponentSerializer.parseChar(c).decoration());
            }
        }

        for (int x = 0; x < text.length(); x++) {
            TextColor color = TextColor.color(HSVLike.of(hues[x] / 255f, sat / 255f, val / 255f));
            component = component.append(Component.text(text.charAt(x), color));
        }

        return component;
    }

    @Override
    public Component process(String in, String format) {
        Matcher matcher = pattern.matcher(in);
        if (!matcher.find()) return null;
        int hue1 = Integer.parseInt(matcher.group(1).substring(0, 2), 16);
        int sat = Integer.parseInt(matcher.group(1).substring(2, 4), 16);
        int val = Integer.parseInt(matcher.group(1).substring(4, 6), 16);

        int hue2 = Integer.parseInt(matcher.group(4), 16);
        String text = matcher.group(3);
        String formats = format;
        if (formats == null) formats = matcher.group(2);

        return fade(hue1, hue2, sat, val, text, formats);
    }
}
