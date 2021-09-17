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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static net.kyori.adventure.text.format.TextDecoration.BOLD;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;
import static net.lucypoulton.squirtgun.discord.adventure.DiscordComponentSerializer.INSTANCE;

public class DeserialiserTests {

    @Test
    public void testBasicText() {
        Assertions.assertEquals(Component.text("hello"), INSTANCE.deserialize("hello"));
    }

    @Test
    public void testPartialFormatted() {
        Assertions.assertEquals(Component.empty().children(List.of(
                        Component.text("one").decorate(BOLD),
                        Component.text(" two"))),
                INSTANCE.deserialize("**one** two"));
    }

    @Test
    public void testNested() {
        Assertions.assertEquals(Component.empty().children(List.of(
                        Component.text("one "),
                        Component.text("two ").decorate(BOLD),
                        Component.text("three").decorate(BOLD).decorate(ITALIC))),
                INSTANCE.deserialize("one **two *three***")
        );
    }

    @Test
    public void testOverriddenFormat() {
        Assertions.assertEquals(Component.empty().children(List.of(
                Component.text("one").decorate(BOLD),
                Component.text("two"),
                Component.text("three").decorate(BOLD),
                Component.text("four")
        )), INSTANCE.deserialize("**one**two**three**four"));
    }
}