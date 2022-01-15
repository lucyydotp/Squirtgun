package net.lucypoulton.squirtgun.tests.format;

import net.lucypoulton.squirtgun.format.node.TextNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static net.lucypoulton.squirtgun.format.FormattingFlag.BOLD;
import static net.lucypoulton.squirtgun.format.FormattingFlag.ITALIC;
import static net.lucypoulton.squirtgun.format.serialiser.MarkdownSerialiser.markdown;
import static net.lucypoulton.squirtgun.format.serialiser.PlainTextSerialiser.plain;

/**
 * Tests for the {@link net.lucypoulton.squirtgun.format.serialiser.PlainTextSerialiser}
 * and {@link net.lucypoulton.squirtgun.format.serialiser.MarkdownSerialiser}.
 */
public class SerialiserTests {
    @Test
    public void testSimpleSerialise() {
        TextNode[] node = {TextNode.text("hello "), TextNode.text("world!")};
        Assertions.assertEquals("hello world!", plain().serialise(node));
        Assertions.assertEquals("hello world!", markdown().serialise(node));
    }

    @Test
    public void testBasicDecorations() {
        TextNode component = TextNode.decorated("a", BOLD, ITALIC);

        /*
        the order of decorators is not predictable, so instead we check
        that three asterisks and two underscores are present
         */
        String result = markdown().serialise(component).split("a")[0];

        Assertions.assertEquals(3, result.length());
        Assertions.assertEquals(1, result.chars().filter(ch -> ch == '_').count());
        Assertions.assertEquals(2, result.chars().filter(ch -> ch == '*').count());
    }

    @Test
    public void testCommonFormatters() {
        TextNode component = TextNode.ofChildren(
                TextNode.decorated("one", BOLD),
                TextNode.decorated("two", BOLD, ITALIC),
                TextNode.decorated("three", BOLD)
        );
        Assertions.assertEquals("**one_two_three**", markdown().serialise(component));
    }

    @Test
    public void testInheritedFormatters() {
        TextNode component = TextNode.ofChildren(Set.of(BOLD),
                TextNode.text("one"),
                TextNode.decorated("two", ITALIC),
                TextNode.text("three"));

        Assertions.assertEquals("**one_two_three**", markdown().serialise(component));
    }
}


