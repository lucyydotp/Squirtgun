package net.lucypoulton.squirtgun.format.serialiser;

import net.lucypoulton.squirtgun.format.node.TextNode;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Serialises a set of nodes to Discord-flavoured Markdown. All formatting information is lost.
 */
public class PlainTextSerialiser implements TextSerialiser<String> {

    private static final PlainTextSerialiser instance = new PlainTextSerialiser();

    public static PlainTextSerialiser plain() {
        return instance;
    }

    private PlainTextSerialiser() {
    }

    @Override
    public String serialise(TextNode... nodes) {
        return Arrays.stream(nodes)
                .map(node -> node.content() + serialise(node.children().toArray(new TextNode[0])))
                .collect(Collectors.joining());
    }
}
