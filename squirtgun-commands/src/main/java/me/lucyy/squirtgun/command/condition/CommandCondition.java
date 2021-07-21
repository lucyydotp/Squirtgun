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
package me.lucyy.squirtgun.command.condition;

import me.lucyy.squirtgun.platform.audience.PermissionHolder;
import me.lucyy.squirtgun.platform.audience.SquirtgunPlayer;

/**
 * TODO javadoc
 */
@FunctionalInterface
public interface CommandCondition {

    static CommandCondition empty() {
        return holder -> true;
    }

    static CommandCondition hasPermission(String permission) {
        return holder -> holder.hasPermission(permission);
    }

    static CommandCondition isPlayer() {
        return holder -> holder instanceof SquirtgunPlayer;
    }

    static CommandCondition isConsole() {
        return holder -> !(holder instanceof SquirtgunPlayer);
    }

    boolean canExecute(PermissionHolder holder);

    default CommandCondition and(CommandCondition next) {
        return holder -> this.canExecute(holder) && next.canExecute(holder);
    }

    default CommandCondition or(CommandCondition next) {
        return holder -> this.canExecute(holder) || next.canExecute(holder);
    }

    default CommandCondition inverse() {
        return holder -> !this.canExecute(holder);
    }
}
