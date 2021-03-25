package me.lucyy.common.format;

import net.md_5.bungee.api.ChatColor;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlockedGradient implements FormatPattern {
    private final Pattern pattern = Pattern.compile("\\{flag:([A-Za-z]+):?([klmno]+)?>}(.*)\\{flag<}");
    private final Map<String, ChatColor[]> colours;

    public BlockedGradient(Map<String, ChatColor[]> colours) {
        for (ChatColor[] array : colours.values())
            if (array.length < 2) throw new IllegalArgumentException("Expected at least 2 colours");
        this.colours = colours;
    }

    @Override
    public String process(String in, String overrideFormatter) {
        Matcher matcher = pattern.matcher(in);

        while (matcher.find()) {
            String label = matcher.group(1);
            String formattersRaw = overrideFormatter == null ? matcher.group(2) : overrideFormatter;
            StringBuilder formatters = new StringBuilder();
            if (formattersRaw != null) {
                for (char character : formattersRaw.toLowerCase().toCharArray()) formatters.append("&").append(character);
            }
            String content = matcher.group(3);

            ChatColor[] cols = colours.get(label);
            if (cols == null) continue;

            int stepsPerChar = Math.max(content.length() / cols.length, 1);
            float padding = content.length() > 5 ? (content.length() % cols.length) / 2.0f : 0;
            int startPadding = (int) Math.floor(padding);

            StringBuilder out = new StringBuilder(cols[0].toString())
                    .append(content, 0, stepsPerChar + startPadding);
            int strIndex = stepsPerChar + startPadding;
            for (int i = 1; i < cols.length - 1; i++) {
                try {
                    String sub = content.substring(strIndex, strIndex + stepsPerChar);
                    out.append(cols[i]).append(formatters).append(sub);
                } catch (StringIndexOutOfBoundsException ignored) {
                }
                strIndex += stepsPerChar;
            }
            if (strIndex != content.length()) out.append(cols[cols.length - 1]).append(content.substring(strIndex));
            in = in.replace(matcher.group(), out.toString());
        }
        return in;
    }
}
