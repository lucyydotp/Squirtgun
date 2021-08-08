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

package me.lucyy.squirtgun.fabric.task;

import io.netty.util.concurrent.DefaultThreadFactory;
import me.lucyy.squirtgun.fabric.FabricPlatform;
import me.lucyy.squirtgun.platform.scheduler.Task;
import me.lucyy.squirtgun.platform.scheduler.TaskScheduler;
import net.minecraft.server.MinecraftServer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

/**
 * A simple task scheduler.
 */
public final class FabricTaskScheduler implements TaskScheduler {

    private static final long MSPT = 50L;

    private final FabricPlatform platform;
    private final MinecraftServer server;
    private final Map<Task, ScheduledFuture<?>> taskMap = new LinkedHashMap<>();
    private final ScheduledExecutorService scheduler = newSingleThreadScheduledExecutor(new DefaultThreadFactory("squirtgun-fabric-scheduler"));

    public FabricTaskScheduler(final FabricPlatform platform) {
        this.platform = platform;
        this.server = platform.getServer();
    }

    /**
     * Shuts down and the underlying scheduler, cancels queued tasks and waits for them to
     * finish execution.
     */
    public void shutdown() {
        try {
            this.scheduler.shutdown();
            //noinspection ResultOfMethodCallIgnored
            this.scheduler.awaitTermination(15L, TimeUnit.SECONDS);
        } catch (final InterruptedException exception) {
            // oh well
            exception.printStackTrace();
        }
    }

    @Override
    public void start(final Task task) {
        cancel(task);

        final ScheduledFuture<?> future =
                task.isRepeating()
                        ? this.scheduler.scheduleWithFixedDelay(runTask(task), task.getDelay() * MSPT, task.getInterval() * MSPT, TimeUnit.MILLISECONDS)
                        : this.scheduler.schedule(runTask(task), task.getDelay() * 50L, TimeUnit.MILLISECONDS);
        this.taskMap.put(task, future);
    }

    @Override
    public void cancel(final Task task) {
        Optional.ofNullable(this.taskMap.remove(task)).ifPresent(future -> future.cancel(false));
    }

    private Runnable runTask(final Task task) {
        return () -> {
            if (task.isAsync()) {
                task.execute(this.platform);
            } else {
                this.server.execute(() -> task.execute(this.platform));
            }
        };
    }
}
