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
package net.lucypoulton.squirtgun.command.condition;

import net.lucypoulton.squirtgun.command.PermissionHolder;
import net.lucypoulton.squirtgun.command.context.CommandContext;

/**
 * A condition that must be satisfied in order for a command to run.
 * <p>
 * Conditions are transformative - they have an input and an output type.
 * If the condition is not met, then a message is displayed.
 *
 * @param <T> the input holder type
 * @param <U> the outputted holder type
 */
@FunctionalInterface
public interface Condition<T extends PermissionHolder, U extends PermissionHolder> {

    /**
     * A condition that will always pass, carrying out no casting.
     */
    static <V extends PermissionHolder> Condition<V, V> alwaysTrue() {
        return (target, context) -> new Result<>(true, target, null);
    }

    /**
     * A condition that checks for a permission.
     */
    static <V extends PermissionHolder> Condition<V, V> hasPermission(String permission) {
        return (target, context) -> target.hasPermission(permission)
            ? new Result<>(true, target, null)
            : new Result<>(false, null, "No permission!");
    }

    /**
     * Test the condition.
     * @param target the target to test against
     * @param context the command context
     * @return a {@link Result<U>} that has a non-null result when successful, and a non-null error when not
     */
    Result<U> test(T target, CommandContext context);

    /**
     * Combines two conditions with the same output type and a contravariant input type in such a way that the test
     * passes when both tests are successful.
     *
     * @param other the second test
     * @return a condition that passes when both conditions pass
     */
    default <V extends U> Condition<T, V> and(Condition<? super U, V> other) {
        return ((target, context) -> {
            Result<U> first = this.test(target, context);
            if (!first.isSuccessful()) {
                return new Result<>(false, null, first.getError());
            }

            return other.test(first.getResult(), context);
        });
    }

    /**
     * Combines two conditions with the same output type and a contravariant input type in such a way that the test
     * passes when either or both tests are successful.
     *
     * @param other the second test
     * @return a condition that passes when either or both conditions pass
     */
    default Condition<T, U> or(Condition<? super T, ? extends U> other) {
        return ((target, context) -> {
            Result<U> first = this.test(target, context);
            if (first.isSuccessful()) {
                return first;
            }

            // thank you type erasure very cool
            Result<? extends U> otherResult = other.test(target,context);
            return new Result<>(otherResult.successful, otherResult.result, otherResult.error);
        });
    }

    /**
     * The result of a test.
     * @param <U> the output type of the test
     */
    class Result<U> {
        private final boolean successful;
        private final U result;
        private final String error;

        public Result(boolean successful, U result, String error) {
            this.successful = successful;
            this.result = result;
            this.error = error;
        }

        /**
         * Whether the test passed.
         */
        public boolean isSuccessful() {
            return successful;
        }

        /**
         * Whether the test failed - inverse of {@link #isSuccessful()}.
         */
        public final boolean isFailure() {
            return !isSuccessful();
        }

        /**
         * Gets the result of the test. When the test has failed, this may be null.
         */
        public U getResult() {
            return result;
        }

        /**
         * Gets the error, if any, that this test encountered, When the test has passed,
         * this may be null.
         */
        public String getError() {
            return error;
        }
    }
}
