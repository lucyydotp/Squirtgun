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
