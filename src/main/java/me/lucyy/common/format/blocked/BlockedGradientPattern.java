package me.lucyy.common.format.blocked;

import me.lucyy.common.format.pattern.FormatPattern;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class BlockedGradientPattern implements FormatPattern {
    private final Pattern pattern;
    private final Map<String, ChatColor[]> colours = new HashMap<>();

    public BlockedGradientPattern(String name, BlockedGradient... gradients) {
        pattern = Pattern.compile("\\{" + name + ":([A-Za-z]+):?([klmno]+)?>}(.*)\\{" + name + "<}");
        for (BlockedGradient gradient : gradients) {
            for (String gradName : gradient.getNames()) colours.put(gradName, gradient.getCols());
        }
    }

    @Override
    public Component process(String in, String overrideFormatter) {
        return null;
        /* TODO
        Matcher matcher = pattern.matcher(in);

        while (matcher.find()) {
            String label = matcher.group(1);
            String formattersRaw = overrideFormatter == null ? matcher.group(2) : overrideFormatter;
            StringBuilder formatters = new StringBuilder();
            if (formattersRaw != null) {
                for (char character : formattersRaw.toLowerCase().toCharArray())
                    formatters.append("&").append(character);
            }
            String content = matcher.group(3);

            ChatColor[] cols = colours.get(label);
            if (cols == null) continue;

            StringBuilder out = new StringBuilder();

            float step = content.length() / (float)cols.length;
            Map<Integer, ChatColor> colourPoints = new HashMap<>();
            float i = 0;
            do {
                colourPoints.put(Math.round(i), cols[(int)(i / step)]);
                i += step;
            } while (i < content.length());

            for (int j = 0; j < content.length(); j++) {
                if (colourPoints.containsKey(j)) out.append(colourPoints.get(j)).append(formatters);
                out.append(content.charAt(j));
            }

            in = in.replace(matcher.group(), out.toString());
        }
        return in;*/
    }
}
