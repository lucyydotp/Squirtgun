package me.lucyy.squirtgun.platform.scheduler;

/**
 * Responsible for scheduling tasks to occur later.
 * Heavily inspired by Bukkit's scheduler.
 */
public interface TaskScheduler {

	/**
	 * Schedules a task for execution.
	 *
	 * @param task the task to run
	 */
	void start(Task task);

	/**
	 * Cancels a task.
	 *
	 * @param task the task to cancel
	 */
	void cancel(Task task);
}
