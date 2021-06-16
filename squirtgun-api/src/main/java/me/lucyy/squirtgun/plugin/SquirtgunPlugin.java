package me.lucyy.squirtgun.plugin;

import com.google.common.base.Preconditions;
import me.lucyy.squirtgun.platform.Platform;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

/**
 * A plugin written using the Squirtgun API.
 *
 * @param <P> a Platform. If not required, use the default platform implementation.
 * @since 2.0.0
 */
public abstract class SquirtgunPlugin<P extends Platform> {

    private final P platform;

    public SquirtgunPlugin(@NotNull P platform) {
        Preconditions.checkNotNull(platform);
        this.platform = platform;
    }

    /**
     * Logs a prefixed text message to the console. TODO - do we really need this?
     *
     * @param text the text to log
     */
    public void log(String text) {
        getPlatform().log(Component.text("[" + getPluginName() + "] " + text));
    }

    /**
     * Gets the platform-specific object provided in the constructor.
     */
    public @NotNull P getPlatform() {
        return platform;
    }

    /**
     * Gets the plugin's name.
     */
    public abstract @NotNull String getPluginName();

    /**
     * Gets the plugin's version.
     */
    public abstract @NotNull String getPluginVersion();

    /**
     * Gets a list of the authors' names.
     */
    public abstract @NotNull String[] getAuthors();
}
