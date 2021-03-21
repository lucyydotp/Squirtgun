package me.lucyy.common.tests;

import me.lucyy.common.format.TextFormatter;
import net.md_5.bungee.api.ChatColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FormatterTests {
    String generateMcFormat(String in) {
        StringBuilder builder = new StringBuilder();
        builder.append(ChatColor.COLOR_CHAR).append("x");

        for (char stringChar : in.toCharArray()) {
            builder.append(ChatColor.COLOR_CHAR).append(stringChar);
        }
        return builder.toString();
    }

    @Test
    @DisplayName("Ensure that vanilla formatters with & work")
    public void testVanillaFormat() {
        Assertions.assertEquals("" + ChatColor.GREEN + ChatColor.BOLD + "te" + ChatColor.AQUA + "st",
                TextFormatter.format("&a&lte&bst"));
    }

    @Test
    @DisplayName("Ensure that single hex colours work")
    public void testHexFormat() {
        String expectedOut = generateMcFormat("ff00ff") + "test";

        Assertions.assertEquals(expectedOut, TextFormatter.format("{#ff00ff}test"));
        Assertions.assertEquals(expectedOut, TextFormatter.format("{#FF00FF}test"));
    }

    @Test
    @DisplayName("Ensure RGB gradients are calculated correctly")
    public void testRgbGradient() {
        String out = generateMcFormat("00fe00") + "a"
                + generateMcFormat("807f80") + "b"
                + generateMcFormat("ff00ff") + "c";

        Assertions.assertEquals(out, TextFormatter.format("{#00fe00>}abc{#ff00ff<}"));
    }

    @Test
    @DisplayName("Ensure HSV gradients are calculated correctly")
    public void testHsvGradient() {
        String out = generateMcFormat("ff0000") + "a"
                + generateMcFormat("ffb400") + "b"
                + generateMcFormat("96ff00") + "c";

        Assertions.assertEquals(out, TextFormatter.format("{hsv:00ffff>}abc{3c<}"));
    }

    @Test
    @DisplayName("Ensure fade function is working properly")
    public void testFade() {
        int[] actual = TextFormatter.fade(5, 0, 40);
        int[] expected = new int[] {0, 10, 20, 30, 40};

        for (int x = 0; x < expected.length; x++) {
            Assertions.assertEquals(actual[x], expected[x]);
        }
    }

    @Test
    @DisplayName("Ensure text is centred properly")
    public void testCentreText() {
        Assertions.assertEquals(
                "§e                                §f test 12345 §e                                ",
        TextFormatter.centreText("test 12345", new TestFormatter(), " "));
    }
}