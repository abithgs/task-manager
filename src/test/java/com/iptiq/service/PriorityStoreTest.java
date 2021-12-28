package com.iptiq.service;

import com.iptiq.model.Process;
import com.iptiq.repository.PriorityStore;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PriorityStoreTest {

    @Test
    void shouldInitialisePriorityStore() {
        PriorityStore store = new PriorityStore();

        assertThat(store.fetchProcessesByPriority(Process.Priority.HIGH)).isEmpty();
        assertThat(store.fetchProcessesByPriority(Process.Priority.MEDIUM)).isEmpty();
        assertThat(store.fetchProcessesByPriority(Process.Priority.LOW)).isEmpty();
    }

    @Test
    void shouldAddNewProcessToPriorityBucket() {
        PriorityStore store = new PriorityStore();

        store.addProcess(Process.create(Process.Priority.HIGH));
        store.addProcess(Process.create(Process.Priority.MEDIUM));
        store.addProcess(Process.create(Process.Priority.LOW));
        store.addProcess(Process.create(Process.Priority.LOW));

        assertThat(store.fetchProcessesByPriority(Process.Priority.HIGH)).hasSize(1);
        assertThat(store.fetchProcessesByPriority(Process.Priority.MEDIUM)).hasSize(1);
        assertThat(store.fetchProcessesByPriority(Process.Priority.LOW)).hasSize(2);
    }

    @Test
    void shouldRemoveProcessToPriorityBucket() {
        PriorityStore store = new PriorityStore();

        Process process = Process.create(Process.Priority.HIGH);
        store.addProcess(process);
        assertThat(store.fetchProcessesByPriority(Process.Priority.HIGH)).hasSize(1);

        store.removeProcess(process);

        assertThat(store.fetchProcessesByPriority(Process.Priority.HIGH)).isEmpty();
    }

    @Test
    void shouldClearPriority() {
        PriorityStore store = new PriorityStore();

        store.addProcess(Process.create(Process.Priority.HIGH));
        store.addProcess(Process.create(Process.Priority.MEDIUM));
        store.addProcess(Process.create(Process.Priority.LOW));
        store.addProcess(Process.create(Process.Priority.LOW));

        assertThat(store.fetchProcessesByPriority(Process.Priority.HIGH)).hasSize(1);
        assertThat(store.fetchProcessesByPriority(Process.Priority.MEDIUM)).hasSize(1);
        assertThat(store.fetchProcessesByPriority(Process.Priority.LOW)).hasSize(2);

        store.clearPriority(Process.Priority.LOW);

        assertThat(store.fetchProcessesByPriority(Process.Priority.HIGH)).hasSize(1);
        assertThat(store.fetchProcessesByPriority(Process.Priority.MEDIUM)).hasSize(1);
        assertThat(store.fetchProcessesByPriority(Process.Priority.LOW)).isEmpty();
    }

}