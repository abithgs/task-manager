package com.iptiq.service;

import com.iptiq.exception.TaskManagerFullException;
import com.iptiq.model.Process;

import java.util.List;

public interface TaskManager {

    Process add(Process.Priority priority) throws TaskManagerFullException;

    List<Process> list();

    Process kill(long processId);

    void killAll();

    void killGroup(Process.Priority priority);
}
