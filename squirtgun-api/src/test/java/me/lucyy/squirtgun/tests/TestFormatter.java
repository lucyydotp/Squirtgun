package me.lucyy.squirtgun.tests;

import me.lucyy.squirtgun.command.FormatProvider;
import me.lucyy.squirtgun.format.TextFormatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

public class TestFormatter implements FormatProvider {

    @Override
    public Component formatMain(@NotNull String input, TextDecoration[] formatters) {
        Component out = Component.text(input, NamedTextColor.WHITE);
        for (TextDecoration deco : formatters) out = out.decorate(deco);
        return out;
    }

    @Override
    public Component formatAccent(@NotNull String input, TextDecoration[] formatters) {
        Component out = Component.text(input, NamedTextColor.YELLOW);
        for (TextDecoration deco : formatters) out = out.decorate(deco);
        return out;
    }

    @Override
    public Component getPrefix() {
        return TextFormatter.format("{#9dacfa>}CommonLibTest{#fa9efa<} {#a0a0a0} >> ");
    }
}
