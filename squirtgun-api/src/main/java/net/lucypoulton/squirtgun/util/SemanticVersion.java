package net.lucypoulton.squirtgun.util;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A semantic version.
 *
 * @see <a href="https://semver.org">https://semver.org</a>
 */
public class SemanticVersion implements Comparable<SemanticVersion> {

    private static final Pattern parsePattern =
            Pattern.compile("^(?<major>\\d+)\\.(?<minor>\\d+)\\.(?<patch>\\d+)(-(?<prerelease>[0-9A-Za-z-.]+))?(\\+(?<metadata>[0-9A-Za-z-.]+))?$");

    private static final Pattern validityPattern = Pattern.compile("^[0-9A-Za-z-.]$");

    public static SemanticVersion parse(CharSequence input) {
        Matcher matcher = parsePattern.matcher(input);
        if (!matcher.find()) throw new IllegalArgumentException("Not a valid semantic version");
        String prerelease = matcher.group("prerelease");

        return new SemanticVersion(
                Integer.parseInt(matcher.group("major")),
                Integer.parseInt(matcher.group("minor")),
                Integer.parseInt(matcher.group("patch")),
                matcher.group("metadata"),
                prerelease == null ? new String[]{} : prerelease.split("\\.")
        );
    }

    private final int major;
    private final int minor;
    private final int patch;
    private final String[] prerelease;
    private final @Nullable String build;

    public SemanticVersion(int major, int minor, int patch, String... prerelease) {
        this(major, minor, patch, null, prerelease);
    }

    public SemanticVersion(int major, int minor, int patch, @Nullable String build, String... prerelease) {
        Preconditions.checkArgument(major >= 0, "Major version cannot be below 0");
        Preconditions.checkArgument(minor >= 0, "Minor version cannot be below 0");
        Preconditions.checkArgument(patch >= 0, "Patch version cannot be below 0");
        Preconditions.checkArgument(Arrays.stream(prerelease).allMatch(validityPattern.asMatchPredicate()),
                "Prerelease contains invalid characters");
        if (build != null) {
            Preconditions.checkArgument(validityPattern.matcher(build).matches(),
                    "Build metadata contains invalid characters");
        }
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.prerelease = prerelease;
        this.build = build;
    }

    public int major() {
        return major;
    }

    public int minor() {
        return minor;
    }

    public int patch() {
        return patch;
    }

    public String[] prerelease() {
        return prerelease;
    }

    public @Nullable String build() {
        return build;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SemanticVersion) {
            SemanticVersion ver = (SemanticVersion) obj;
            return this.major() == ver.major()
                    && this.minor() == ver.minor()
                    && this.patch() == ver.patch()
                    && Arrays.equals(this.prerelease(), ver.prerelease());
        }
        return false;
    }

    @Override
    public int compareTo(@NotNull SemanticVersion o) {
        int major = Integer.compare(this.major(), o.major());
        if (major != 0) return major;

        int minor = Integer.compare(this.minor(), o.minor());
        if (minor != 0) return minor;

        int patch = Integer.compare(this.patch(), o.patch());
        if (patch != 0) return patch;

        int highest = Math.min(o.prerelease().length, this.prerelease().length);

        if (highest == 0) {
            return Integer.compare(o.prerelease().length, this.prerelease().length);
        }

        for (int i = 0; i < highest; i++) {
            int prerelease = o.prerelease()[i].compareTo(this.prerelease()[i]);
            if (prerelease != 0) return prerelease;
        }

        return highest;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder()
                .append(major()).append(".")
                .append(minor()).append(".")
                .append(patch());
        if (prerelease().length != 0) {
            out.append("-").append(String.join(".", prerelease()));
        }
        if (build() != null) {
            out.append("+").append(build());
        }
        return out.toString();
    }
}
