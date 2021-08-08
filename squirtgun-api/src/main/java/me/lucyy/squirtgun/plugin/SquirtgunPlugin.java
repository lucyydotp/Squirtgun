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

    /**
     * Called when the plugin is enabled. By default, does nothing.
     */
    public void onEnable() {
    }

    /**
     * Called when the plugin is disabled. By default, does nothing.
     */
    public void onDisable() {
    }
}
