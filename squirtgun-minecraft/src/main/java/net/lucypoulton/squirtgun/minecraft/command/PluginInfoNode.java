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

package net.lucypoulton.squirtgun.minecraft.command;

import net.lucypoulton.squirtgun.Squirtgun;
import net.lucypoulton.squirtgun.command.PermissionHolder;
import net.lucypoulton.squirtgun.command.condition.Condition;
import net.lucypoulton.squirtgun.command.context.CommandContext;
import net.lucypoulton.squirtgun.command.node.AbstractNode;
import net.lucypoulton.squirtgun.format.FormatProvider;
import net.lucypoulton.squirtgun.format.node.TextNode;
import net.lucypoulton.squirtgun.minecraft.plugin.SquirtgunPlugin;

import java.util.List;

public class PluginInfoNode extends AbstractNode<PermissionHolder> {

    private final SquirtgunPlugin<?> plugin;

    public PluginInfoNode(String name, SquirtgunPlugin<?> plugin) {
        super(name, "Shows information about this plugin.", Condition.alwaysTrue());
        this.plugin = plugin;
    }

    @Override
    public TextNode execute(CommandContext context) {
        final FormatProvider format = context.getFormat();
        return TextNode.ofChildren(
                format.title(plugin.getPluginName()),
                format.main(plugin.getPluginName() + " version "),
                format.accent(plugin.getPluginVersion() + "\n"),
                format.main("Written by "),
                format.accent(String.join(", ", plugin.getAuthors()) + "\n"),
                format.main("Built with Squirtgun version "),
                format.accent(Squirtgun.VERSION.toString()),
                format.main(", using platform "),
                format.accent(plugin.getPlatform().name())
        );
    }
}
