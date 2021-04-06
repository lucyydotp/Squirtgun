package me.lucyy.common.format.blocked;

import me.lucyy.common.format.pattern.FormatPattern;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlockedGradientPattern implements FormatPattern {
    private final Pattern pattern;
    private final Map<String, TextColor[]> colours = new HashMap<>();

    public BlockedGradientPattern(String name, BlockedGradient... gradients) {
        pattern = Pattern.compile("\\{" + name + ":([A-Za-z]+):?([klmno]+)?>}(.*)\\{" + name + "<}");
        for (BlockedGradient gradient : gradients) {
            for (String gradName : gradient.getNames()) colours.put(gradName, gradient.getCols());
        }
    }

    private String safeSubstr(String in, int i, int l) {
        return l >= in.length() ? in.substring(i) : in.substring(i, l);
    }

    @Override
    public Component process(@NotNull String in, String overrideFormatter) {
        Matcher matcher = pattern.matcher(in);
        if (!matcher.find()) return null;

        String label = matcher.group(1);

        Component component = Component.empty();
        String formats = overrideFormatter == null ? matcher.group(2) : overrideFormatter;
        if (formats != null) {
            for (char c : formats.toCharArray()) {
                //noinspection ConstantConditions - the char is checked in regex
                component = component.decorate(LegacyComponentSerializer.parseChar(c).decoration());
            }
        }
        String content = matcher.group(3);

        TextColor[] cols = colours.get(label);
        if (cols == null) return Component.text(content);

        float step = content.length() / (float) cols.length;
        List<Integer> points = new ArrayList<>();
        for (float i = 0; i < content.length(); i += step) {
            points.add(Math.round(i));
        }

        for (int i = 0; i < points.size(); i++) {
            String text = i + 1 == points.size() ?
                    content.substring(points.get(i)) : safeSubstr(content, points.get(i), points.get(i + 1));
            component = component.append(Component.text(text, cols[i]));
        }
        return component;
    }
}
