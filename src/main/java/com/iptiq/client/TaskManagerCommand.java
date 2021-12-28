package com.iptiq.client;

import com.iptiq.exception.InvalidCommandInputException;
import com.iptiq.exception.TaskManagerFullException;
import com.iptiq.model.Process;
import com.iptiq.service.TaskManager;

public enum TaskManagerCommand {

    ADD {
        @Override
        void validateArgs(String input) throws InvalidCommandInputException {
            validatePriority(input);
        }

        @Override
        void processCommand(TaskManager taskManager, String input) throws InvalidCommandInputException {
            validateArgs(input);
            try {
                System.out.println(taskManager.add(Process.Priority.valueOf(input.toUpperCase())));
            } catch (TaskManagerFullException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }, KILL {
        @Override
        void validateArgs(String input) throws InvalidCommandInputException {
            try {
                Long.parseLong(input);
            } catch (NumberFormatException nfe) {
                throw new InvalidCommandInputException("Invalid pid, Enter a numeric value");
            }
        }

        @Override
        void processCommand(TaskManager taskManager, String input) throws InvalidCommandInputException {
            validateArgs(input);
            System.out.println(taskManager.kill(Long.parseLong(input)));
        }
    }, LIST {
        @Override
        void validateArgs(String input) {
            //NOP
        }

        @Override
        void processCommand(TaskManager taskManager, String input) {
            System.out.println(taskManager.list());
        }
    }, KILLGROUP {
        @Override
        void validateArgs(String input) throws InvalidCommandInputException {
            TaskManagerCommand.validatePriority(input);
        }

        @Override
        void processCommand(TaskManager taskManager, String input) throws InvalidCommandInputException {
            validateArgs(input);
            taskManager.killGroup(Process.Priority.valueOf(input.toUpperCase()));
            System.out.println(taskManager.list());
        }
    }, KILLALL {
        @Override
        void validateArgs(String input) {
            //NOP
        }

        @Override
        void processCommand(TaskManager taskManager, String input) {
            taskManager.killAll();
            System.out.println(taskManager.list());
        }
    };

    private static void validatePriority(String input) throws InvalidCommandInputException {
        try {
            Process.Priority.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException exe) {
            throw new InvalidCommandInputException("Invalid priority , Accepted values LOW | MEDIUM | HIGH");
        }
    }

    abstract void validateArgs(String input) throws InvalidCommandInputException;

    abstract void processCommand(TaskManager taskManager, String input) throws InvalidCommandInputException;
}
