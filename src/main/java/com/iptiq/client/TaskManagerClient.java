package com.iptiq.client;

import com.iptiq.service.DefaultTaskManager;
import com.iptiq.service.FifoTaskManager;
import com.iptiq.service.PriorityTaskManager;
import com.iptiq.service.TaskManager;

import java.util.Scanner;

public class TaskManagerClient {

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            String taskManagerOption = readTaskManagerOption(input);
            String capacity = readTaskManagerCapacity(input);
            TaskManager taskManager = taskManagerStrategy(taskManagerOption, capacity);
            TaskMangerOperations.processCommand(taskManager);
        }
    }

    private static TaskManager taskManagerStrategy(String taskManagerOption, String capacity) {
        switch (Integer.parseInt(taskManagerOption)) {
            case 2:
                return new FifoTaskManager(Integer.parseInt(capacity));
            case 3:
                return new PriorityTaskManager(Integer.parseInt(capacity));
            default:
                return new DefaultTaskManager(Integer.parseInt(capacity));
        }
    }

    private static String readTaskManagerOption(Scanner input) {
        System.out.println("Select TaskManager type to be used :\n" +
                "1. Default Taskmanager\n" +
                "2. FIFO Taskmanager\n" +
                "3. Priority based Taskmanager");
        String taskManagerOption = input.nextLine();
        while (!isNumericAndWithinRange(taskManagerOption, 3)) {
            System.out.println("Invalid input!!, Select among above options");
            taskManagerOption = input.nextLine();
        }
        return taskManagerOption;
    }

    private static String readTaskManagerCapacity(Scanner input) {
        System.out.println("Enter MAX capacity of TaskManager : ");
        String capacity = input.nextLine();
        while (!isNumericAndWithinRange(capacity, Integer.MAX_VALUE)) {
            System.out.println("Invalid input!!, Capacity should be positive and less than Integer Max value");
            capacity = input.nextLine();
        }
        return capacity;
    }

    private static boolean isNumericAndWithinRange(String option, int range) {
        if (option == null) {
            return false;
        }
        try {
            int selected = Integer.parseInt(option);
            if (selected > 0 && selected <= range) {
                return true;
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
        return false;
    }
}
