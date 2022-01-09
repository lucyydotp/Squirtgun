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
package net.lucypoulton.squirtgun.tests.minecraft;

import net.lucypoulton.squirtgun.minecraft.platform.event.Event;
import net.lucypoulton.squirtgun.minecraft.platform.event.EventHandler;
import net.lucypoulton.squirtgun.minecraft.platform.event.EventManager;
import net.lucypoulton.squirtgun.minecraft.platform.event.EventPriority;
import net.lucypoulton.squirtgun.minecraft.platform.event.cancellable.AbstractCancellableEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventTests {

    static class DummyEvent implements Event {
    }

    static class DummyCancellableEvent extends AbstractCancellableEvent {
    }

    private final List<Integer> list = new ArrayList<>();

    private final EnumMap<EventPriority, EventHandler<?>> handlers = new EnumMap<>(EventPriority.class);

    {
        handlers.put(EventPriority.HIGHEST, EventHandler.builder(DummyEvent.class)
            .priority(EventPriority.HIGHEST)
            .handle(x -> list.add(0))
            .build());
        handlers.put(EventPriority.HIGH, EventHandler.builder(DummyEvent.class)
            .priority(EventPriority.HIGH)
            .handle(x -> list.add(1))
            .build());
        handlers.put(EventPriority.NORMAL, EventHandler.builder(DummyEvent.class)
            .priority(EventPriority.NORMAL)
            .handle(x -> list.add(2))
            .build());
        handlers.put(EventPriority.LOW, EventHandler.builder(DummyEvent.class)
            .priority(EventPriority.LOW)
            .handle(x -> list.add(3))
            .build());
        handlers.put(EventPriority.LOWEST, EventHandler.builder(DummyEvent.class)
            .priority(EventPriority.LOWEST)
            .handle(x -> list.add(4))
            .build());
    }

    @BeforeEach
    public void setup() {
        list.clear();
    }

    @Test
    public void testEventOrder() {
        EventManager manager = new EventManager(new DummyPlatform());
        for (EventHandler<?> key : handlers.values()) {
            manager.register(key);
        }
        manager.dispatch(new DummyEvent());
        Assertions.assertEquals(List.of(0, 1, 2, 3, 4), list);
    }

    @Test
    public void testCancellable() {
        EventManager manager = new EventManager(new DummyPlatform());

        AtomicBoolean lastHandlerExec = new AtomicBoolean(false);

        manager.register(EventHandler.builder(DummyCancellableEvent.class)
            .priority(EventPriority.HIGHEST)
            .handle(AbstractCancellableEvent::cancel)
            .build()
        );
        manager.register(EventHandler.builder(DummyCancellableEvent.class)
            .priority(EventPriority.NORMAL)
            .handle(x -> Assertions.fail())
            .build()
        );
        manager.register(EventHandler.builder(DummyCancellableEvent.class)
            .priority(EventPriority.LOW)
            .executeOnCancel()
            .handle(x -> lastHandlerExec.set(true))
            .build()
        );

        manager.dispatch(new DummyCancellableEvent());

        Assertions.assertTrue(lastHandlerExec.get());
    }

    @Test
    public void testMultipleEventsWithSamePriority() {
        EventManager manager = new EventManager(new DummyPlatform());
        final Set<String> set = new HashSet<>();
        manager.register(EventHandler.executes(DummyEvent.class, e -> set.add("one")));
        manager.register(EventHandler.executes(DummyEvent.class, e -> set.add("two")));
        manager.register(EventHandler.executes(DummyEvent.class, e -> set.add("three")));
        manager.dispatch(new DummyEvent());
        Assertions.assertEquals(Set.of("one", "two", "three"), set);
    }
}
