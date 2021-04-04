package me.lucyy.common.format.pattern;

import me.lucyy.common.format.TextFormatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.Nullable;

public class LegacyAmpersandPattern implements FormatPattern {
    @Override
    public @Nullable Component process(String in, String formattersOverride) {
        if (!in.contains("&")) return null;
        return TextFormatter.applyLegacyDecorations(
                LegacyComponentSerializer.legacyAmpersand().deserialize(in),
                formattersOverride);
    }
}
