package me.lucyy.squirtgun.bukkit.task;

import me.lucyy.squirtgun.bukkit.BukkitPlatform;
import me.lucyy.squirtgun.platform.scheduler.Task;
import me.lucyy.squirtgun.platform.scheduler.TaskScheduler;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class BukkitTaskScheduler implements TaskScheduler {

	private final Map<Task, BukkitTask> taskMap = new HashMap<>();
	private final BukkitPlatform platform;

	public BukkitTaskScheduler(BukkitPlatform platform) {
		this.platform = platform;
	}

	BukkitTask register(Task task) {

		BukkitScheduler sched = Bukkit.getScheduler();

		if (task.isAsync()) {
			if (task.isRepeating()) {
				return sched.runTaskTimerAsynchronously(
						platform.getBukkitPlugin(),
						() -> task.execute(platform),
						task.getDelay(),
						task.getInterval()
				);
			}
			return sched.runTaskLaterAsynchronously(
					platform.getBukkitPlugin(),
					() -> task.execute(platform),
					task.getDelay()
			);
		}

		if (task.isRepeating()) {
			return sched.runTaskTimer(
					platform.getBukkitPlugin(),
					() -> task.execute(platform),
					task.getDelay(),
					task.getInterval()
			);
		}
		return sched.runTaskLater(
				platform.getBukkitPlugin(),
				() -> task.execute(platform),
				task.getDelay()
		);
	}

	@Override
	public void start(Task task) {
		BukkitTask bukkitTask = register(task);
		taskMap.put(task, bukkitTask);
	}

	@Override
	public void cancel(Task task) {
		@Nullable BukkitTask bukkitTask = taskMap.get(task);
		if (bukkitTask == null) {
			return;
		}
		bukkitTask.cancel();
		taskMap.remove(task);
	}
}
