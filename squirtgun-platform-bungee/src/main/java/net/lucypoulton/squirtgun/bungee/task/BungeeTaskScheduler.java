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

package net.lucypoulton.squirtgun.bungee.task;

import net.lucypoulton.squirtgun.bungee.BungeePlatform;
import net.lucypoulton.squirtgun.minecraft.platform.scheduler.Task;
import net.lucypoulton.squirtgun.minecraft.platform.scheduler.TaskScheduler;
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
