import org.apache.commons.lang3.ArrayUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Scanner;

public class TaskManager {

    static String[][] tasks;

    public static void main(String[] args) {
        String fileName = "tasks.csv";
        loadTasks(fileName);
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        do {
            System.out.println(ConsoleColors.BLUE + "Please select an option: " + ConsoleColors.WHITE);
            System.out.println("add");
            System.out.println("remove");
            System.out.println("list");
            System.out.println("exit");

            String option = scanner.nextLine();
            switch (option) {
                case "add":
                    addTask();
                    break;
                case "remove":
                    removeTask();
                    break;
                case "list":
                    listTasks();
                    break;
                case "exit":
                    exit = true;
                    break;
                default:
                    System.out.println("Please select a correct option.");
            }
        } while (!exit);
        saveTasks(fileName);
        System.out.println(ConsoleColors.RED + "Bye, bye.");
    }

    private static void saveTasks(String fileName) {
        try {
            Path path = Path.of(fileName);
           Files.writeString(path, "", StandardOpenOption.TRUNCATE_EXISTING);
            for (int i = 0; i < tasks.length; i++) {
                String[] task = tasks[i];
                String line = task[0] + ", " + task[1] + ", " + task[2] + "\r\n";
                Files.writeString(path, line, StandardOpenOption.APPEND);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void removeTask() {
        Scanner scanner = new Scanner(System.in);
        Integer indexToRemove = null;
        do {
            System.out.println("Please select number to remove:");
            try {
                int tempInt = Integer.parseInt(scanner.nextLine());
                if (tempInt >= 0 && tempInt < tasks.length) {
                    indexToRemove = tempInt;
                }
            } catch (NumberFormatException e) {
            }
            if (indexToRemove == null) {
                System.out.println("Incorrect argument passed. Please give number >= 0");
            }
        } while (indexToRemove == null);
        tasks = ArrayUtils.remove(tasks, indexToRemove);
    }

    private static void addTask() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please add task description:");
        String description = scanner.nextLine();
        System.out.println("Please add task due date:");
        String dueDate = scanner.nextLine();
        System.out.println("Is your task is important: true/false:");
        String isImportant = scanner.nextLine();
        tasks = Arrays.copyOf(tasks, tasks.length + 1);
        tasks[tasks.length - 1] = new String[]{description, dueDate, isImportant};
    }

    private static void listTasks() {
        for (int i = 0; i < tasks.length; i++) {
            String[] task = tasks[i];
            System.out.print("[" + i + "]: ");
            for (String taskCell : task) {
                System.out.print(taskCell + "\t");
            }
            System.out.println();
        }
    }

    private static void loadTasks(String fileName) {
        tasks = new String[0][3];
        try (Scanner reader = new Scanner(new FileReader(fileName))) {
            int i = 0;
            while (reader.hasNextLine()) {
                tasks = Arrays.copyOf(tasks, tasks.length + 1);
                String line = reader.nextLine();
                tasks[i] = line.split(", ");
                i++;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Unable to read file");
        }
    }
}
