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

package me.lucyy.squirtgun.sponge.task;

import me.lucyy.squirtgun.sponge.SpongePlatform;
import me.lucyy.squirtgun.platform.scheduler.Task;
import me.lucyy.squirtgun.platform.scheduler.TaskScheduler;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.ScheduledTask;
import org.spongepowered.api.util.Ticks;

import java.util.HashMap;
import java.util.Map;

public class SpongeTaskScheduler implements TaskScheduler {

    private final Map<Task, ScheduledTask> taskMap = new HashMap<>();
    private final SpongePlatform platform;

    public SpongeTaskScheduler(SpongePlatform platform) {
        this.platform = platform;
    }

    // all tasks will be run asynchronously as bungee does not support sync scheduling
    ScheduledTask register(Task task) {

        org.spongepowered.api.scheduler.Task.Builder builder = org.spongepowered.api.scheduler.Task.builder()
                .execute(() -> task.execute(platform))
                .plugin(platform.getPlugin())
                .delay(Ticks.of(task.getDelay()));

        if (task.isRepeating()) {
            builder = builder.interval(Ticks.of(task.getInterval()));
        }

        return Sponge.server().scheduler().submit(builder.build());
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
