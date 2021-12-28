package com.iptiq.exception;

import com.iptiq.model.Process;

public class TaskManagerFullException extends Exception {

    public TaskManagerFullException(Process.Priority priority) {
        super("TaskManager FULL, Could not add process with Priority " + priority);
    }
}
