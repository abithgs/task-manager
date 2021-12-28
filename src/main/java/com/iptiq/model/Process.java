package com.iptiq.model;

import java.util.concurrent.atomic.AtomicLong;

public final class Process {

    private static final AtomicLong COUNTER = new AtomicLong(0);

    private final long pid;
    private final Priority priority;

    private Process(Priority priority) {
        this.pid = COUNTER.incrementAndGet();
        this.priority = priority;
    }

    public static Process create(Priority priority) {
        return new Process(priority);
    }

    public long getPid() {
        return pid;
    }

    public Priority getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return "Process{" +
                "pid=" + pid +
                ", priority=" + priority +
                '}';
    }

    public enum Priority {
        LOW, MEDIUM, HIGH
    }
}
