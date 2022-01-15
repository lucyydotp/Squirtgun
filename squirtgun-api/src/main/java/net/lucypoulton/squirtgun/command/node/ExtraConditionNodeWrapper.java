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
package net.lucypoulton.squirtgun.command.node;

import net.lucypoulton.squirtgun.command.PermissionHolder;
import net.lucypoulton.squirtgun.command.condition.Condition;
import net.lucypoulton.squirtgun.command.context.CommandContext;
import net.lucypoulton.squirtgun.format.node.TextNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class ExtraConditionNodeWrapper<T extends PermissionHolder> implements CommandNode<T> {

    private final CommandNode<T> parent;
    private final Condition<? super T, ? extends T> condition;

    ExtraConditionNodeWrapper(CommandNode<T> parent, Condition<? super T, ? extends T> condition) {
        this.parent = parent;
        this.condition = condition;
    }

    @Override
    public @Nullable TextNode[] execute(CommandContext context) {
        return parent.execute(context);
    }

    @Override
    public @NotNull String getName() {
        return parent.getName();
    }

    @Override
    public String getDescription() {
        return parent.getDescription();
    }

    @Override
    public @NotNull Condition<PermissionHolder, ? extends T> getCondition() {
        return (target, context) -> {
            Condition.Result<? extends T> result = parent.getCondition().test(target, context);
            return condition.test(result.getResult(), context).isSuccessful()
                // java generics are *quirky*
                ? new Condition.Result<>(result.isSuccessful(), result.getResult(), result.getError())
                : new Condition.Result<>(false, null, null);
        };
    }
}
