package net.lucypoulton.squirtgun.format.serialiser;

import net.lucypoulton.squirtgun.format.FormattingFlag;
import net.lucypoulton.squirtgun.format.node.TextNode;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import static net.lucypoulton.squirtgun.format.FormattingFlag.BOLD;
import static net.lucypoulton.squirtgun.format.FormattingFlag.CODE;
import static net.lucypoulton.squirtgun.format.FormattingFlag.ITALIC;
import static net.lucypoulton.squirtgun.format.FormattingFlag.SPOILER;
import static net.lucypoulton.squirtgun.format.FormattingFlag.STRIKETHROUGH;
import static net.lucypoulton.squirtgun.format.FormattingFlag.UNDERLINE;

/**
 * Serialises a set of nodes to Discord-flavoured Markdown. Colour information is lost.
 */
public class MarkdownSerialiser implements TextSerialiser<String> {
    private static final MarkdownSerialiser instance = new MarkdownSerialiser();

    private static final Map<FormattingFlag, String> MARKUP = Map.of(
            BOLD, "**",
            ITALIC, "_",
            UNDERLINE, "__",
            STRIKETHROUGH, "~~",
            SPOILER, "||",
            CODE, "`"
    );

    public static MarkdownSerialiser markdown() {
        return instance;
    }

    private MarkdownSerialiser() {
    }

    private Map<String, Set<FormattingFlag>> flatten(TextNode node, Set<FormattingFlag> parentFlags) {

        Map<String, Set<FormattingFlag>> out = new LinkedHashMap<>();

        parentFlags = new HashSet<>(parentFlags);
        parentFlags.addAll(node.formattingFlags());
        out.put(node.content(), parentFlags);

        for (TextNode child : node.children()) {
            out.putAll(flatten(child, parentFlags));
        }
        return out;
    }

    private Map<String, Set<FormattingFlag>> flatten(TextNode[] nodes) {
        Map<String, Set<FormattingFlag>> flags = new LinkedHashMap<>();
        for (TextNode node : nodes) {
            flags.put(node.content(), node.formattingFlags());
            flags.putAll(flatten(node, new HashSet<>()));
        }
        return flags;
    }

    @Override
    public String serialise(TextNode... nodes) {

        Stack<FormattingFlag> currentFlags = new Stack<>();
        StringBuilder output = new StringBuilder();

        for (Map.Entry<String, Set<FormattingFlag>> node : flatten(nodes).entrySet()) {

            // finish all decorations that aren't applicable
            while (!currentFlags.isEmpty() && !node.getValue().contains(currentFlags.peek())) {
                FormattingFlag deco = currentFlags.pop();
                output.append(MARKUP.get(deco));
            }

            // add any missing decorations
            node.getValue().stream().filter(deco -> !currentFlags.contains(deco)).forEach(deco -> {
                currentFlags.push(deco);
                output.append(MARKUP.get(deco));
            });

            // add the text
            output.append(node.getKey());
        }

        // finish any outstanding decorations
        while (!currentFlags.isEmpty()) {
            output.append(MARKUP.get(currentFlags.pop()));
        }
        return output.toString();
    }
}