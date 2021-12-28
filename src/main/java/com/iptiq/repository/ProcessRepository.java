package com.iptiq.repository;

import com.iptiq.model.Process;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * Wrapper arround backing datastructures for Process store and Priority store. This supports simple synchronised operation
 * for all methods. Room for improvement using concurrent packages and read and write locks.
 */
public class ProcessRepository {

    private final Map<Long, Process> processStore;
    private final PriorityStore priorityStore;

    public ProcessRepository(Map<Long, Process> processStore) {
        this.processStore = processStore;
        this.priorityStore = new PriorityStore();
    }

    public synchronized Process addProcess(Process process) {
        processStore.put(process.getPid(), process);
        priorityStore.addProcess(process);
        return process;
    }

    public synchronized Process removeProcess(long processId) {
        Process deletedProcess = processStore.remove(processId);
        Optional.ofNullable(deletedProcess)
                .ifPresent(priorityStore::removeProcess);
        return deletedProcess;
    }

    public synchronized void clear() {
        processStore.clear();
        priorityStore.clear();
    }

    public synchronized List<Process> findAll() {
        return List.copyOf(processStore.values());
    }

    public synchronized void removeProcessGroup(Process.Priority priority) {
        priorityStore.fetchProcessesByPriority(priority)
                .forEach(processStore::remove);
        priorityStore.clearPriority(priority);
    }

    public synchronized int size() {
        return processStore.size();
    }

    public synchronized Long findFirstKey() {
        if (processStore instanceof TreeMap) {
            return ((TreeMap<Long, Process>) processStore).firstKey();
        }
        return processStore.entrySet().iterator().next().getKey();
    }

    public synchronized List<Long> findProcessGroup(Process.Priority priority) {
        return priorityStore.fetchProcessesByPriority(priority);
    }

}
