package net.lucypoulton.squirtgun.minecraft.platform.audience;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.lucypoulton.squirtgun.format.node.TextNode;
import org.jetbrains.annotations.NotNull;

/**
 * A serialiser to convert between Squirtgun rich text nodes and Adventure components.
 * TODO - make this work properly
 */
public class AdventureComponentSerialiser implements ComponentSerializer<TextComponent, TextComponent, TextNode[]> {

    @Override
    public @NotNull TextComponent deserialize(TextNode @NotNull [] input) {
        return null;
    }

    @Override
    public TextNode @NotNull [] serialize(@NotNull TextComponent component) {
        return null;
    }
}
