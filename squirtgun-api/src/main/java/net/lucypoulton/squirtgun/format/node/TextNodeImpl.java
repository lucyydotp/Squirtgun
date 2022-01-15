package net.lucypoulton.squirtgun.format.node;

import net.lucypoulton.squirtgun.format.FormattingFlag;
import net.lucypoulton.squirtgun.format.colour.Colourful;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

class TextNodeImpl implements TextNode {

    private final String content;
    private final Set<FormattingFlag> formattingFlags;
    private final Colourful colour;
    private final List<TextNode> children;

    TextNodeImpl(@NotNull String content, @NotNull Set<FormattingFlag> formattingFlags, @NotNull Colourful colour, List<TextNode> children) {
        this.content = content;
        this.formattingFlags = formattingFlags;
        this.colour = colour;
        this.children = children;
    }

    @Override
    @NotNull
    public String content() {
        return content;
    }

    @Override
    @NotNull
    public Set<FormattingFlag> formattingFlags() {
        return formattingFlags;
    }

    @Override
    @NotNull
    public Colourful colour() {
        return colour;
    }

    @Override
    public @NotNull List<TextNode> children() {
        return children;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TextNodeImpl node &&
                node.content().equals(content()) &&
                node.formattingFlags().equals(formattingFlags()) &&
                node.colour() == colour();
    }
}
