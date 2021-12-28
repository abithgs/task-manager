package com.iptiq.service;

import com.iptiq.exception.TaskManagerFullException;
import com.iptiq.model.Process;

import java.util.LinkedHashMap;

/**
 * Default implementation of {@link TaskManager}. Process store is backed by {@link LinkedHashMap}.
 * This provides constant add and kill operations. Order of insertion is maintained for list.
 * KillGroup is backed by {@link com.iptiq.repository.PriorityStore}. This supports constant time.
 */
public class DefaultTaskManager extends AbstractTaskManager {

    private final int capacity;

    public DefaultTaskManager(int capacity) {
        super(new LinkedHashMap<>(capacity));
        this.capacity = capacity;
    }

    @Override
    public Process add(Process.Priority priority) throws TaskManagerFullException {
        if (isCapacityReached(capacity)) {
            throw new TaskManagerFullException(priority);
        }
        return processRepository.addProcess(Process.create(priority));
    }
}
