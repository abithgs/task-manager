package com.iptiq.service;

import com.iptiq.model.Process;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FifoTaskManagerTest {

    @Test
    void shouldAddNewProcessToStore() {
        FifoTaskManager taskManager = new FifoTaskManager(10);

        Process process1 = taskManager.add(Process.Priority.HIGH);
        Process process2 = taskManager.add(Process.Priority.LOW);

        assertThat(taskManager.list()).hasSize(2)
                .containsExactly(process1, process2);
    }

    @Test
    void shouldRemoveOldestWhenQueueFull() {
        FifoTaskManager taskManager = new FifoTaskManager(2);

        taskManager.add(Process.Priority.HIGH);
        taskManager.add(Process.Priority.LOW);
        Process process3 = taskManager.add(Process.Priority.MEDIUM);
        Process process4 = taskManager.add(Process.Priority.HIGH);

        assertThat(taskManager.list()).hasSize(2).containsExactly(process3, process4);


    }

    @Test
    void shouldKillProcessWithPid() {
        FifoTaskManager taskManager = new FifoTaskManager(10);

        Process process1 = taskManager.add(Process.Priority.HIGH);
        Process process2 = taskManager.add(Process.Priority.LOW);

        assertThat(taskManager.kill(process1.getPid())).isEqualTo(process1);

        assertThat(taskManager.list()).hasSize(1)
                .contains(process2);
    }

    @Test
    void shouldKillAllProcesses() {
        FifoTaskManager taskManager = new FifoTaskManager(10);

        taskManager.add(Process.Priority.HIGH);
        taskManager.add(Process.Priority.LOW);

        taskManager.killAll();

        assertThat(taskManager.list()).isEmpty();
    }

    @Test
    void shouldKillProcessGroup() {
        FifoTaskManager taskManager = new FifoTaskManager(10);

        Process process1 = taskManager.add(Process.Priority.HIGH);
        Process process2 = taskManager.add(Process.Priority.MEDIUM);
        Process process3 = taskManager.add(Process.Priority.MEDIUM);

        taskManager.killGroup(Process.Priority.MEDIUM);

        assertThat(taskManager.list()).hasSize(1).contains(process1);
    }

}