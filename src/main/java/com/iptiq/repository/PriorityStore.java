package com.iptiq.repository;

import com.iptiq.model.Process;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Stream;

/**
 * Stores all processId's in an EnumMap . Each priority bucket contains list of processId's in order of insertion.
 * This supports constant operation for killGroup commands. Returns all processId's under a priority.
 */
public final class PriorityStore {

    private final EnumMap<Process.Priority, HashSet<Long>> priorityBucket;

    public PriorityStore() {
        this.priorityBucket = new EnumMap<>(Process.Priority.class);
        Stream.of(Process.Priority.values())
                .forEach(priority -> this.priorityBucket.put(priority, new LinkedHashSet<>()));
    }

    public void addProcess(Process process) {
        priorityBucket.get(process.getPriority()).add(process.getPid());
    }

    public void removeProcess(Process process) {
        priorityBucket.get(process.getPriority()).remove(process.getPid());
    }


    public List<Long> fetchProcessesByPriority(Process.Priority priority) {
        return List.copyOf(priorityBucket.get(priority));
    }

    public void clearPriority(Process.Priority priority) {
        priorityBucket.get(priority).clear();
    }

    public void clear() {
        Stream.of(Process.Priority.values())
                .forEach(priority -> this.priorityBucket.put(priority, new HashSet<>()));
    }

}
