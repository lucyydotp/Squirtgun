package net.lucypoulton.squirtgun.format.node;

import net.lucypoulton.squirtgun.format.FormattingFlag;
import net.lucypoulton.squirtgun.format.colour.Colourful;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class ContainerNode implements TextNode {

    private final Set<FormattingFlag> formattingFlags;
    private final Colourful colour;
    private final List<TextNode> children;

    ContainerNode(Set<FormattingFlag> formattingFlags, Colourful colour, List<TextNode> children) {
        this.formattingFlags = formattingFlags;
        this.colour = colour;
        this.children = children;
    }

    @Override
    public @NotNull String content() {
        return "";
    }

    @Override
    public @NotNull Set<FormattingFlag> formattingFlags() {
        return formattingFlags;
    }

    @Override
    public @NotNull Colourful colour() {
        return colour;
    }

    @Override
    public @NotNull List<TextNode> children() {
        return children;
    }
}
