package com.iptiq.service;

import com.iptiq.model.Process;
import com.iptiq.repository.ProcessRepository;

import java.util.List;
import java.util.Map;

public abstract class AbstractTaskManager implements TaskManager {

    protected final ProcessRepository processRepository;

    protected AbstractTaskManager(Map<Long, Process> mapStore) {
        this.processRepository = new ProcessRepository(mapStore);
    }

    @Override
    public List<Process> list() {
        return List.copyOf(processRepository.findAll());
    }

    @Override
    public Process kill(long processId) {
        return processRepository.removeProcess(processId);
    }

    @Override
    public void killAll() {
        processRepository.clear();
    }

    @Override
    public void killGroup(Process.Priority priority) {
        processRepository.removeProcessGroup(priority);
    }

    public boolean isCapacityReached(int capacity) {
        return processRepository.size() == capacity;
    }
}
