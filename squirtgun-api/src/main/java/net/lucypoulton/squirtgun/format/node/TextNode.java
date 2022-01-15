package net.lucypoulton.squirtgun.format.node;

import net.lucypoulton.squirtgun.format.FormattingFlag;
import net.lucypoulton.squirtgun.format.colour.Colour;
import net.lucypoulton.squirtgun.format.colour.Colourful;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * A rich text element.
 */
public interface TextNode {
    @NotNull String content();

    @NotNull Set<FormattingFlag> formattingFlags();

    @NotNull Colourful colour();

    @NotNull List<TextNode> children();

    Colour DEFAULT_COLOUR = new Colour(255, 255, 255);

    static TextNode text(String content) {
        return new TextNodeImpl(content, Set.of(), DEFAULT_COLOUR, List.of());
    }

    static TextNode decorated(String content, FormattingFlag... flags) {
        return new TextNodeImpl(content, Set.of(flags), DEFAULT_COLOUR, List.of());
    }

    static TextNode ofChildren(TextNode... nodes) {
        return new ContainerNode(Set.of(), DEFAULT_COLOUR, Arrays.asList(nodes));
    }

    static TextNode ofChildren(Colourful colour, TextNode... nodes) {
        return new ContainerNode(Set.of(), DEFAULT_COLOUR, Arrays.asList(nodes));
    }

    static TextNode ofChildren(Set<FormattingFlag> formattingFlags, TextNode... nodes) {
        return new ContainerNode(formattingFlags, DEFAULT_COLOUR, Arrays.asList(nodes));
    }
}
