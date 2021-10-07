/*
 * Copyright © 2021 Lucy Poulton
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package net.lucypoulton.squirtgun.discord.tests;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.lucypoulton.squirtgun.discord.adventure.DiscordComponentSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static net.lucypoulton.squirtgun.discord.adventure.DiscordComponentSerializer.INSTANCE;

public class SerialiserTests {
    @Test
    public void testBasicText() {
        Assertions.assertEquals("hello",
            INSTANCE.serialize(Component.text("hello")));
    }

    @Test
    public void testBasicDecorations() {
        Component component = Component.text("a")
            .decorate(TextDecoration.BOLD)
            .decorate(TextDecoration.ITALIC)
            .decorate(TextDecoration.UNDERLINED);

        /*
        the order of decorators is not predictable, so instead we check
        that three asterisks and two underscores are present
         */
        String result = INSTANCE.serialize(component).split("a")[0];

        Assertions.assertEquals(5, result.length());
        Assertions.assertEquals(2, result.chars().filter(ch -> ch == '_').count());
        Assertions.assertEquals(3, result.chars().filter(ch -> ch == '*').count());
    }

    @Test
    public void testInheritedDecorations() {
        Component component = Component.text("a")
            .decorate(TextDecoration.BOLD)
            .children(List.of(Component.text("b")));

        Assertions.assertEquals("**ab**", INSTANCE.serialize(component));
    }

    @Test
    public void testOverriddenDecorations() {
        Component component = Component.text("a")
            .decorate(TextDecoration.BOLD)
            .children(List.of(Component.text("b").decoration(TextDecoration.BOLD, false)));

        Assertions.assertEquals("**a**b", INSTANCE.serialize(component));
    }

    @Test
    public void testNestedOverrides() {
        Component component = Component.text("a")
            .decorate(TextDecoration.BOLD)
            .children(List.of(
                Component.text("b").decoration(TextDecoration.BOLD, false).children(List.of(
                    Component.text("c").decoration(TextDecoration.BOLD, true).children(List.of(
                        Component.text("d").decoration(TextDecoration.BOLD, false)
                    ))
                ))
            ));

        Assertions.assertEquals("**a**b**c**d", INSTANCE.serialize(component));
    }

    @Test
    public void testChildDecorations() {
        Component component = Component.text("a")
            .decorate(TextDecoration.BOLD)
            .children(List.of(
                Component.text("b").decorate(TextDecoration.UNDERLINED)
            ));

        Assertions.assertEquals("**a__b__**", INSTANCE.serialize(component));
    }

    @Test
    public void testNestedInheritance() {
        Component component = Component.text("a").decorate(TextDecoration.BOLD)
                .children(List.of(
                        Component.text("b").children(List.of(
                                Component.text("c").children(List.of(
                                        Component.text("d").children(List.of(
                                                Component.text("e")
                                        ))
                                ))
                        ))
                ));

        Assertions.assertEquals("**abcde**", INSTANCE.serialize(component));
    }

    @Test
    public void testBasicDeserialise() {
        Assertions.assertEquals(INSTANCE.deserialize("**hello**"),
                Component.text("hello").decorate(TextDecoration.BOLD));
    }
}
