package net.lucypoulton.squirtgun.tests.format;

import net.lucypoulton.squirtgun.format.FormatProvider;
import net.lucypoulton.squirtgun.format.FormattingFlag;
import net.lucypoulton.squirtgun.format.node.TextNode;

public class TestFormatter implements FormatProvider {
    @Override
    public TextNode prefix() {
        return TextNode.text("");
    }

    @Override
    public TextNode main(String content) {
        return TextNode.text(content);
    }

    @Override
    public TextNode accent(String content) {
        return TextNode.decorated(content, FormattingFlag.UNDERLINE);
    }

    @Override
    public TextNode title(String content) {
        return TextNode.decorated(content, FormattingFlag.BOLD);
    }

    @Override
    public TextNode subtitle(String content) {
        return TextNode.decorated(content, FormattingFlag.ITALIC);
    }
}
