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

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * A task that can be run. It can be scheduled to run later, or to repeat itself, and it may be asynchronous.
 */
public class Task {

	public static Builder builder() {
		return new Builder();
	}

	public static Builder builder(final Consumer<Platform> action) {
		return new Builder().action(action);
	}

	public static Builder builder(final Runnable action) {
		return new Builder().action(action);
	}

	/**
	 * A builder for a task.
	 */
	public static class Builder {

		private static final AtomicLong ID_SUPPLIER = new AtomicLong(0L);

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

		public Builder action(Runnable action) {
			this.action = p -> action.run();
			return this;
		}

		public Task build() {
			Preconditions.checkNotNull(action, "Action has not been set");
			return new Task(action, delay, interval, isAsync, ID_SUPPLIER.getAndIncrement());
		}
	}

	private final Consumer<Platform> action;
	private final int delay;
	private final int interval;
	private final boolean isAsync;
	private final long id;

	/**
	 * Creates a new task.
	 *
	 * @param action   the action to execute
	 * @param delay    how long to wait before executing this task
	 * @param interval how many ticks to wait between executing repeatedly, or -1 if it should not repeat.
	 * @param isAsync  whether to run this task asynchronously - how this happens is dictated by the platform.
	 */
	private Task(final Consumer<Platform> action, final int delay, final int interval, final boolean isAsync, final long id) {
		this.action = action;
		this.delay = delay;
		this.interval = interval;
		this.isAsync = isAsync;
		this.id = id;
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

	public long getId() {
		return this.id;
	}

	@Override
	public String toString() {
		return "Task{"
					 + "id=" + this.id
					 + ", action=" + this.action
					 + ", delay=" + this.delay
					 + ", interval=" + this.interval
					 + ", isAsync=" + this.isAsync
					 + '}';
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other) { return true; }
		if (!(other.getClass() == this.getClass())) { return false; }

		final Task that = (Task) other;
		return this.delay == that.delay
					 && this.interval == that.interval
					 && this.isAsync == that.isAsync
					 && this.id == that.id;
	}

	@Override
	public int hashCode() {
		int result = this.delay;
		result = 31 * result + this.interval;
		result = 31 * result + (this.isAsync ? 1 : 0);
		result = 31 * result + (int) (this.id ^ (this.id >>> 32));
		return result;
	}
}
