package me.lucyy.common.format.pattern;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

public interface FormatPattern {
	@Nullable
    default Component process(String in) {
        return process(in, null);
    }

    @Nullable
    Component process(String in, String formattersOverride);
}
