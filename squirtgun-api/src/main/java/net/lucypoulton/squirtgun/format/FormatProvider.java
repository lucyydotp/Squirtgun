package net.lucypoulton.squirtgun.format;

import net.lucypoulton.squirtgun.format.node.TextNode;

/**
 * TODO javadoc
 */
public interface FormatProvider {
    TextNode prefix();

    TextNode main(String content);

    TextNode accent(String content);

    TextNode title(String content);

    TextNode subtitle(String content);
}
