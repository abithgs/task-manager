package com.iptiq.service;

import com.iptiq.exception.TaskManagerFullException;
import com.iptiq.model.Process;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PriorityTaskManagerTest {

    @Test
    void shouldAddNewProcessToStore() throws TaskManagerFullException {
        PriorityTaskManager taskManager = new PriorityTaskManager(10);

        Process process1 = taskManager.add(Process.Priority.HIGH);
        Process process2 = taskManager.add(Process.Priority.LOW);

        assertThat(taskManager.list()).hasSize(2)
                .containsExactly(process1, process2);
    }

    @Test
    void shouldRemoveOldestLeastPriority() throws TaskManagerFullException {
        PriorityTaskManager taskManager = new PriorityTaskManager(2);

        Process process1 = taskManager.add(Process.Priority.HIGH);
        Process process2 = taskManager.add(Process.Priority.LOW);
        Process process3 = taskManager.add(Process.Priority.MEDIUM);
        assertThat(taskManager.list()).hasSize(2).containsExactly(process1, process3);

        Process process4 = taskManager.add(Process.Priority.HIGH);

        assertThat(taskManager.list()).hasSize(2).containsExactly(process1, process4);
    }

    @Test
    void shouldRemoveOnlyOldestLeastPriority() throws TaskManagerFullException {
        PriorityTaskManager taskManager = new PriorityTaskManager(4);

        Process process1 = taskManager.add(Process.Priority.HIGH);
        Process process2 = taskManager.add(Process.Priority.LOW);
        Process process3 = taskManager.add(Process.Priority.MEDIUM);
        Process process4 = taskManager.add(Process.Priority.LOW);

        Process process5 = taskManager.add(Process.Priority.HIGH);

        assertThat(taskManager.list()).hasSize(4).containsExactly(process1, process3, process4, process5);
    }

    @Test
    void shouldThrowExceptionOnSamePriority() throws TaskManagerFullException {
        DefaultTaskManager taskManager = new DefaultTaskManager(2);

        Process process1 = taskManager.add(Process.Priority.HIGH);
        Process process2 = taskManager.add(Process.Priority.MEDIUM);

        assertThrows(TaskManagerFullException.class, () -> taskManager.add(Process.Priority.LOW));
        assertThat(taskManager.list()).hasSize(2).containsExactly(process1, process2);
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