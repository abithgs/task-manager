package com.iptiq.service;

import com.iptiq.exception.TaskManagerFullException;
import com.iptiq.model.Process;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Priority TaskManager implementation of {@link TaskManager}. Process store is backed by {@link LinkedHashMap}.
 * This provides constant time performance of add and kill operations. Order of insertion is maintained for list.
 * KillGroup is backed by {@link com.iptiq.repository.PriorityStore}. This supports constant time.
 */
public class PriorityTaskManager extends AbstractTaskManager {

    private final int capacity;

    public PriorityTaskManager(int capacity) {
        super(new LinkedHashMap<>(capacity));
        this.capacity = capacity;
    }

    @Override
    public Process add(Process.Priority priority) throws TaskManagerFullException {
        if (isCapacityReached(capacity)) {
            findLesserPriorityProcesses(priority)
                    .map(this::findOldestTask)
                    .map(processRepository::removeProcess)
                    .orElseThrow(() -> new TaskManagerFullException(priority));
        }
        return processRepository.addProcess(Process.create(priority));
    }

    private Long findOldestTask(List<Long> pids) {
        return pids.get(0);
    }

    private Optional<List<Long>> findLesserPriorityProcesses(Process.Priority priority) {
        return Stream.of(Process.Priority.values())
                .filter(prioEnum -> prioEnum.ordinal() < priority.ordinal())
                .sorted(Comparator.comparing(Process.Priority::ordinal))
                .map(processRepository::findProcessGroup)
                .filter(processList -> !processList.isEmpty())
                .findFirst();
    }
}
