package com.iptiq.service;

import com.iptiq.model.Process;

import java.util.TreeMap;

/**
 * FIFO implementation of {@link TaskManager}. Process store is backed by {@link TreeMap}.
 * This provides better performance of add and kill operations. Order of insertion is maintained for list.
 * KillGroup is backed by {@link com.iptiq.repository.PriorityStore}. This supports constant time.
 */
public class FifoTaskManager extends AbstractTaskManager {

    private final int capacity;

    public FifoTaskManager(int capacity) {
        super(new TreeMap<>());
        this.capacity = capacity;
    }

    @Override
    public Process add(Process.Priority priority) {
        if (isCapacityReached(capacity)) {
            long oldestPid = processRepository.findFirstKey();
            processRepository.removeProcess(oldestPid);
        }
        return processRepository.addProcess(Process.create(priority));
    }
}
