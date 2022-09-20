package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class TaskManager {
    static String[][] taskData;
    static final String FILE_NAME = "tasks.csv";
    public static void main(String[] args) {
        taskData = tasks(FILE_NAME);

        Scanner scan = new Scanner(System.in);
        boolean isQuit = false;
        while (!isQuit) {
            console();
            switch (scan.next()) {
                case "add":
                    addTask();
                    break;
                case "remove":
                    removeTask(taskData, getTheNumber());
                    System.out.println("Chosen task was deleted");
                    break;
                case "list":
                    listTask(taskData);
                    break;
                case "exit":
                    saveTabToFile(FILE_NAME, taskData);
                    System.out.println(ConsoleColors.RED + "Finishing...");
                    isQuit = true;
                    break;
            }
        }
    }

    public static void console() {
        String[] options = {"Please select an option: ", "add", "remove", "list", "exit"};

        System.out.println(ConsoleColors.BLUE + options[0] + ConsoleColors.RESET + "\n" + options[1] + "\n" + options[2] + "\n" + options[3] + "\n" + options[4]);
    }

    public static String[][] tasks(String fileName) {
        File file = new File(fileName);
        StringBuilder reading = new StringBuilder();

        try (Scanner scan = new Scanner(file)) {
            while (scan.hasNextLine()) {
                reading.append(scan.nextLine() + ",");
            }
        } catch (
                FileNotFoundException e) {
            System.err.println("Missing file");
        }

        String verses = reading.toString();

        String[] versesSplited = verses.split(",");

        String[][] task = new String[versesSplited.length / 3][3];
        int l = 0;
        for (int i = 0; i < task.length; i++) {
            for (int j = 0; j < 3; j++) {
                task[i][j] = versesSplited[j + l];
            }
            l += 3;
        }
        return task;
    }

    public static void listTask(String[][] listOfTasks) {
        String list = "";
        for (int i = 0; i < listOfTasks.length; i++) {
            for (int j = 0; j < 3; j++) {
                list += listOfTasks[i][j];
            }
            System.out.println(i + " : " + list);
            list = "";
        }
        System.out.println();
    }

    public static void addTask() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please add task description:");
        String taskDesc = scan.nextLine() + " ";
        System.out.println("Please add task due date");
        String taskDueDate = scan.nextLine() + " ";
        System.out.println("Is your task important?: true/false");
        String taskImportance = scan.nextLine();

        taskData = Arrays.copyOf(taskData, taskData.length + 1);
        taskData[taskData.length-1] = new String[3];
        taskData[taskData.length-1][0] = taskDesc;
        taskData[taskData.length-1][1] = taskDueDate;
        taskData[taskData.length-1][2] = taskImportance;
    }

    public static boolean isNumberGreaterEqualZero(String input) {
        if (NumberUtils.isParsable(input)) {
            return Integer.parseInt(input) >= 0;
        }
        return false;
    }

    public static int getTheNumber() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please select number to remove.");

        String n = scanner.nextLine();
        while (!isNumberGreaterEqualZero(n)) {
            System.out.println("Incorrect argument passed. Please give number greater or equal 0");
            scanner.nextLine();
        }
        return Integer.parseInt(n);
    }

    private static void removeTask(String[][] tab, int index) {
        try {
            if (index < tab.length) {
                taskData = ArrayUtils.remove(tab, index);
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Element not exist in tab");
        }
    }

    public static void saveTabToFile(String fileName, String[][] tab) {
        Path dir = Paths.get(fileName);

        String[] lines = new String[taskData.length];
        for (int i = 0; i < tab.length; i++) {
            lines[i] = String.join(",", tab[i]);
        }

        try {
            Files.write(dir, Arrays.asList(lines));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
