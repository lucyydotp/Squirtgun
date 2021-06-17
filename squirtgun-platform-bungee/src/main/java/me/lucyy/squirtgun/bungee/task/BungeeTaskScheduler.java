package me.lucyy.squirtgun.bungee.task;

import me.lucyy.squirtgun.bungee.BungeePlatform;
import me.lucyy.squirtgun.platform.scheduler.Task;
import me.lucyy.squirtgun.platform.scheduler.TaskScheduler;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BungeeTaskScheduler implements TaskScheduler {

    private final Map<Task, ScheduledTask> taskMap = new HashMap<>();
    private final BungeePlatform platform;

    public BungeeTaskScheduler(BungeePlatform platform) {
        this.platform = platform;
    }

    // all tasks will be run asynchronously as bungee does not support sync scheduling
    ScheduledTask register(Task task) {

        net.md_5.bungee.api.scheduler.TaskScheduler sched = platform.getBungeePlugin().getProxy().getScheduler();

        if (task.isRepeating()) {
            return sched.schedule(
                    platform.getBungeePlugin(),
                    () -> task.execute(platform),
                    task.getDelay() * 100L,
                    task.getInterval() * 100L,
                    TimeUnit.MILLISECONDS
            );
        }
        return sched.schedule(
                platform.getBungeePlugin(),
                () -> task.execute(platform),
                task.getDelay() * 100L,
                TimeUnit.MILLISECONDS
        );

    }

    @Override
    public void start(Task task) {
        ScheduledTask bungeeTask = register(task);
        taskMap.put(task, bungeeTask);
    }

    @Override
    public void cancel(Task task) {
        @Nullable ScheduledTask bungeeTask = taskMap.get(task);
        if (bungeeTask == null) {
            return;
        }
        bungeeTask.cancel();
        taskMap.remove(task);
    }
}
