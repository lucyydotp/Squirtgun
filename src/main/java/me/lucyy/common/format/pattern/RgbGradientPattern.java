package me.lucyy.common.format.pattern;

import me.lucyy.common.format.TextFormatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RgbGradientPattern implements FormatPattern {
    final Pattern pattern = Pattern.compile("\\{(#[A-Fa-f0-9]{6}):?([klmno]+)?>}(.*)\\{(#[A-Fa-f0-9]{6})<}");

    public static int clamp(int in) {
        if (in > 255) return 255;
        return Math.max(in, 0);
    }

    public static Component fade(String col1, String col2, String formats, String text) {
        Component component = Component.empty();

        TextColor color1 = TextColor.fromCSSHexString(col1);
        assert color1 != null;

        // special case for single-length characters
        if (text.length() == 1) return component.append(Component.text(text).color(color1));

        TextColor color2 = TextColor.fromCSSHexString(col2);
        assert color2 != null;

        int[] reds = TextFormatter.fade(text.length(), color1.red(), color2.red());
        int[] greens = TextFormatter.fade(text.length(), color1.green(), color2.green());
        int[] blues = TextFormatter.fade(text.length(), color1.blue(), color2.blue());


        for (int x = 0; x < text.length(); x++) {
            TextColor color = TextColor.color(clamp(reds[x]), clamp(greens[x]), clamp(blues[x]));
            component = component.append(Component.text(text.charAt(x), color));
        }

        return TextFormatter.applyLegacyDecorations(component, formats); // TODO test this
    }

    @Override
    public @Nullable Component process(String in, String formatter) {
        Matcher matcher = pattern.matcher(in);
        if (!matcher.find()) return null;

        String col1 = matcher.group(1);
        String formatters = formatter;
        if (formatters == null) formatters = matcher.group(2);
        String text = matcher.group(3);
        String col2 = matcher.group(4);
        return fade(col1, col2, formatters, text);
    }
}
