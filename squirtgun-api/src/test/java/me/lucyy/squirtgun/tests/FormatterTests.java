package me.lucyy.squirtgun.tests;

import me.lucyy.squirtgun.format.TextFormatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FormatterTests {

    String ser(Component in) {
        return GsonComponentSerializer.gson().serialize(in);
    }

    @Test
    @DisplayName("Ensure that vanilla formatters with & work")
    public void testVanillaFormat() {
        Assertions.assertEquals("{\"extra\":[{\"bold\":true,\"color\":\"green\",\"text\":\"te\"}," +
                        "{\"color\":\"aqua\",\"text\":\"st\"}],\"text\":\"\"}",
                ser(TextFormatter.format("&a&lte&bst")));
    }

    @Test
    @DisplayName("Ensure that single hex colours work")
    public void testHexFormat() {
        String expectedOut = ser(Component.text("test", TextColor.color(255, 0, 255)));

        Assertions.assertEquals(expectedOut, ser(TextFormatter.format("{#ff00ff}test")));
        Assertions.assertEquals(expectedOut, ser(TextFormatter.format("{#FF00FF}test")));
    }

    @Test
    @DisplayName("Ensure RGB gradients are calculated correctly")
    public void testRgbGradient() {
        String out = "{\"extra\":[{\"color\":\"#00fe00\",\"text\":\"a\"},{\"color\":\"#807f80\",\"text\":\"b\"},"
                + "{\"color\":\"#ff00ff\",\"text\":\"c\"}],\"text\":\"\"}";
        Assertions.assertEquals(out, ser(TextFormatter.format("{#00fe00>}abc{#ff00ff<}")));
    }

    @Test
    @DisplayName("Ensure HSV gradients are calculated correctly")
    public void testHsvGradient() {
        String out = "{\"extra\":[{\"color\":\"#ff0000\",\"text\":\"a\"}," +
                "{\"color\":\"#ffb400\",\"text\":\"b\"},{\"color\":\"#95ff00\",\"text\":\"c\"}],\"text\":\"\"}";

        Assertions.assertEquals(out, ser(TextFormatter.format("{hsv:00ffff>}abc{3c<}")));
    }

    @Test
    @DisplayName("Ensure fade function is working properly")
    public void testFade() {
        int[] actual = TextFormatter.fade(5, 0, 40);
        int[] expected = new int[]{0, 10, 20, 30, 40};

        for (int x = 0; x < expected.length; x++) {
            Assertions.assertEquals(actual[x], expected[x]);
        }
    }

    @Test
    @DisplayName("Ensure text is centred properly")
    public void testCentreText() {
        Assertions.assertEquals(
                "{\"strikethrough\":true,\"color\":\"yellow\",\"extra\":[{\"strikethrough\":false," +
                        "\"color\":\"white\",\"text\":\" test 12345 \"},{\"strikethrough\":true,\"color\":\"yellow\"," +
                        "\"text\":\"                                \"}],\"text\":\"                                \"}",
                ser(TextFormatter.formatTitle("test 12345", new TestFormatter())));
    }

    @Test
    @DisplayName("Ensure pretranslated formatters are maintained")
    public void testPretranslated() {
        Assertions.assertEquals("{\"color\":\"green\",\"text\":\"hello world\"}",
                ser(TextFormatter.format("{#ff00ff>}\u00a7ahello world" + "{#00ff00<}",
                        null, true)));
    }

    @Test
    @DisplayName("Ensure pretranslated formatters are removed when disabled")
    public void testPretranslatedDisabled() {
        String out = "{\"extra\":[{\"color\":\"#00fe00\",\"text\":\"a\"},{\"color\":\"#807f80\",\"text\":\"b\"}," +
                "{\"color\":\"#ff00ff\",\"text\":\"c\"}],\"text\":\"\"}";
        Assertions.assertEquals(out,
                ser(TextFormatter.format("{#00fe00>}\u00a7aabc{#ff00ff<}")));
    }

    @Test
    @DisplayName("Ensure block patterns work properly")
    public void testBlockPatterns() {
        Assertions.assertEquals(
                "{\"extra\":[{\"color\":\"#55cdfc\",\"text\":\"0\"},{\"color\":\"#f7a8b8\",\"text\":\"12\"}," +
                        "{\"color\":\"white\",\"text\":\"3\"},{\"color\":\"#f7a8b8\",\"text\":\"45\"}," +
                        "{\"color\":\"#55cdfc\",\"text\":\"6\"}],\"text\":\"\"}",
                ser(TextFormatter.format("{flag:trans>}0123456{flag<}")));
    }

    @Test
    @DisplayName("Ensure block patterns work properly when the content is shorter than the amount of colours")
    public void testShortenedBlockPatterns() {
        Assertions.assertEquals(
                "{\"extra\":[{\"color\":\"#55cdfc\",\"text\":\"0\"},{\"color\":\"#f7a8b8\",\"text\":\"1\"}," +
                        "{\"color\":\"white\",\"text\":\"\"},{\"color\":\"#f7a8b8\",\"text\":\"2\"}," +
                        "{\"color\":\"#55cdfc\",\"text\":\"3\"}],\"text\":\"\"}",
                ser(TextFormatter.format("{flag:trans>}0123{flag<}")));
    }
}