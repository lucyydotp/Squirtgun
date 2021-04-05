package me.lucyy.common.format.pattern;

import me.lucyy.common.format.TextFormatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class HexPattern implements FormatPattern {

    @Override
    public Component process(String in, String formats) {
        if (in.length() < 8) return null;
        if (!in.startsWith("{#") || in.charAt(8) != '}') return null;
        for (int i = 2; i < 8; i++) {
            char c = in.charAt(i);
            if ((c < '0' || c > '9') && (c < 'A' || c > 'F') && (c < 'a' || c > 'f')) return null;
        }
        TextColor colour = TextFormatter.colourFromText(in.substring(1, 8));
        assert colour != null; // this is just to shut intellij up

        Component component = Component.text(in.substring(9), TextColor.color(colour));
        if (formats != null) {
            for (char c : formats.toCharArray()) {
                //noinspection ConstantConditions - the char is checked in regex
                component = component.decorate(LegacyComponentSerializer.parseChar(c).decoration());
            }
        }
        return component;
    }
}
