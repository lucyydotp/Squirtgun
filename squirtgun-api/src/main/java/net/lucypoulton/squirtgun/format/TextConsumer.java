package net.lucypoulton.squirtgun.format;

import net.lucypoulton.squirtgun.format.node.TextNode;

/**
 * An object capable of receiving text nodes as messages.
 */
@FunctionalInterface
public interface TextConsumer {
    void send(TextNode node);
}
