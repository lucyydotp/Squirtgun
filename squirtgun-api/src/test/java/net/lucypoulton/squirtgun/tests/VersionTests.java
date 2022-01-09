package net.lucypoulton.squirtgun.tests;

import net.lucypoulton.squirtgun.util.SemanticVersion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VersionTests {
    @Test
    public void testParseBasic() {
        Assertions.assertEquals(SemanticVersion.parse("1.2.3"), new SemanticVersion(1, 2, 3));
    }

    @Test
    public void testParseWithPrereleaseAndMeta() {
        Assertions.assertEquals(SemanticVersion.parse("1.2.3-pre+meta"),
                new SemanticVersion(1, 2, 3, "meta", new String[]{"pre"}));
    }

    @Test
    public void testFailWithInvalidCharacters() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> SemanticVersion.parse("a.b.c"));
    }

    @Test
    public void testFailWithMissingVersions() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> SemanticVersion.parse("1.2"));
    }

    @Test
    public void testOrderBasic() {
        Assertions.assertTrue(SemanticVersion.parse("1.2.3").compareTo(SemanticVersion.parse("1.2.4")) < 0);
    }

    @Test
    public void testPrereleaseOrder() {
        Assertions.assertTrue(SemanticVersion.parse("1.0.0").compareTo(SemanticVersion.parse("1.0.0-pre")) > 0);
        Assertions.assertTrue(SemanticVersion.parse("1.0.0-a").compareTo(SemanticVersion.parse("1.0.0-b")) > 0);
        Assertions.assertTrue(SemanticVersion.parse("1.0.0-a.a").compareTo(SemanticVersion.parse("1.0.0-a")) > 0);
    }
}
