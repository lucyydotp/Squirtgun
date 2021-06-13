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

package me.lucyy.squirtgun.platform.scheduler;

import com.google.common.base.Preconditions;
import me.lucyy.squirtgun.platform.Platform;

import java.util.function.Consumer;

/**
 * A task that can be run. It can be scheduled to run later, or to repeat itself, and it may be asynchronous.
 */
public class Task {

	public static Builder builder() {
		return new Builder();
	}

	/**
	 * A builder for a task.
	 */
	public static class Builder {
		private int interval = -1;
		private int delay = 0;
		private boolean isAsync = false;
		private Consumer<Platform> action;

		public Builder interval(int interval) {
			Preconditions.checkArgument(interval > 0, "Interval was not greater than 0");
			this.interval = interval;
			return this;
		}

		public Builder delay(int delay) {
			Preconditions.checkArgument(interval > 0, "Delay was not greater than 0");
			this.delay = delay;
			return this;
		}

		public Builder async() {
			this.isAsync = true;
			return this;
		}

		public Builder action(Consumer<Platform> action) {
			this.action = action;
			return this;
		}

		public Task build() {
			Preconditions.checkNotNull(action, "Action has not been set");
			return new Task(action, delay, interval, isAsync);
		}
	}

	private final Consumer<Platform> action;
	private final int delay;
	private final int interval;
	private final boolean isAsync;

	/**
	 * Creates a new task.
	 *
	 * @param action   the action to execute
	 * @param delay    how long to wait before executing this task
	 * @param interval how many ticks to wait between executing repeatedly, or -1 if it should not repeat.
	 * @param isAsync  whether to run this task asynchronously - how this happens is dictated by the platform.
	 */
	public Task(final Consumer<Platform> action, final int delay, final int interval, final boolean isAsync) {
		this.action = action;
		this.delay = delay;
		this.interval = interval;
		this.isAsync = isAsync;
	}

	/**
	 * Whether this task is repeating.
	 */
	public boolean isRepeating() {
		return interval != -1;
	}

	/**
	 * How long to wait before executing this task.
	 */
	public int getDelay() {
		return delay;
	}

	/**
	 * How many ticks to wait between executing repeatedly, or -1 if it should not repeat.
	 */
	public int getInterval() {
		return interval;
	}

	/**
	 * Executes this task.
	 */
	public void execute(Platform platform) {
		action.accept(platform);
	}

	/**
	 * Whether this task should be run asynchronously.
	 */
	public boolean isAsync() {
		return isAsync;
	}
}
