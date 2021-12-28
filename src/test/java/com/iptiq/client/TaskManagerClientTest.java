package com.iptiq.client;

import com.iptiq.service.DefaultTaskManager;
import com.iptiq.service.FifoTaskManager;
import com.iptiq.service.PriorityTaskManager;
import com.iptiq.service.TaskManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class TaskManagerClientTest {

    public static Stream<Arguments> taskManagerProvider() {

        return Stream.of(
                Arguments.of("1", DefaultTaskManager.class),
                Arguments.of("2", FifoTaskManager.class),
                Arguments.of("3", PriorityTaskManager.class)
        );
    }

    @ParameterizedTest
    @MethodSource("taskManagerProvider")
    void shouldGetCorrectStrategy(String option, Class<? extends TaskManager> taskManagerType) {
        TaskManager taskManager = TaskManagerClient.getTaskManagerStrategy(option, "10");

        Assertions.assertThat(taskManager).isInstanceOf(taskManagerType);
    }

}