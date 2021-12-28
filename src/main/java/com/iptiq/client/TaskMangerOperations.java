package com.iptiq.client;

import com.iptiq.exception.InvalidCommandInputException;
import com.iptiq.service.TaskManager;

import java.util.Scanner;

public class TaskMangerOperations {

    public static void processCommand(TaskManager taskManager) {
        try (Scanner input = new Scanner(System.in)) {
            System.out.println("Below are supported TaskManger operations :\n" +
                    "add [Priority] -> Adds a process to task manager with Priority. Supported Priority LOW, MEDIUM, HIGH\n" +
                    "kill [pid] -> Kill a process with id\n" +
                    "list -> List all processes in Task manager\n" +
                    "killGroup [Priority] -> Kills all process with provided priority\n" +
                    "killAll -> Kills all process in Task manager");

            String userInput = input.nextLine();
            while (!userInput.equalsIgnoreCase("EXIT")) {
                if (userInputValid(userInput)) {
                    String[] rawCommand = userInput.split(" ");
                    String argument = rawCommand.length == 2 ? rawCommand[1] : null;
                    executeCommand(taskManager, rawCommand[0], argument);
                } else {
                    System.out.println("Invalid command for task manager");
                }
                userInput = input.nextLine();
            }
        }
    }

    private static void executeCommand(TaskManager taskManager, String s, String argument) {
        try {
            TaskManagerCommand command = TaskManagerCommand.valueOf(s.toUpperCase());
            command.processCommand(taskManager, argument);
        } catch (IllegalArgumentException ex) {
            System.out.println("Unsupported Operation");
        } catch (InvalidCommandInputException ex) {
            System.out.println("Invalid argument supplied to command");
        }
    }

    private static boolean userInputValid(String userInput) {
        return !userInput.isEmpty() && userInput.split(" ").length <= 2;
    }

}
