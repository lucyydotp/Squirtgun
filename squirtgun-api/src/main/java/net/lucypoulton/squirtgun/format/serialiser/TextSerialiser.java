package net.lucypoulton.squirtgun.format.serialiser;

import net.lucypoulton.squirtgun.format.node.TextNode;

/**
 * Serialises a string of {@link TextNode}s to another format.
 * @param <T> the output type
 */
@FunctionalInterface
public interface TextSerialiser<T> {
    T serialise(TextNode... nodes);
}
