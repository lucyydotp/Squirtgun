package me.lucyy.common.format.pattern;

import me.lucyy.common.format.TextFormatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The legacy ampersand pattern.
 * This is an internal class, you shouldn't need to use it.
 */
public class LegacyAmpersandPattern implements FormatPattern {
    @Override
    public @Nullable Component process(final @NotNull String in, final String formattersOverride) {
        if (!in.contains("&")) return null;
        return TextFormatter.applyLegacyDecorations(
                LegacyComponentSerializer.legacyAmpersand().deserialize(in),
                formattersOverride);
    }
}
