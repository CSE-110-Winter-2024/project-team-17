package edu.ucsd.cse110.successorator.lib.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Tasks {

    public static List<Task> reorder(List<Task> tasks, Task target) {
        // Lists for separating unfinished and finished tasks
        List<Task> unfinishedTasks = new ArrayList<>();
        List<Task> finishedTasks = new ArrayList<>();

        // Separate tasks into finished and unfinished lists
        for (Task task : tasks) {
            if (task.finished()) {
                finishedTasks.add(task);
            } else {
                unfinishedTasks.add(task);
            }
        }

        // Sort unfinished tasks based on predefined tag order
        Comparator<Task> tagOrderComparator = (task1, task2) -> {
            String order = "HWSE"; // Define the custom order
            return Integer.compare(order.indexOf(task1.tag()), order.indexOf(task2.tag()));
        };
        unfinishedTasks.sort(tagOrderComparator);

        // Rebuild the tasks list: add all unfinished tasks first, then the finished ones
        List<Task> newTasks = new ArrayList<>();
        newTasks.addAll(unfinishedTasks);
        newTasks.addAll(finishedTasks);

        // Assign new order to tasks based on their new positions
        assignOrder(newTasks);
        return newTasks;
    }

    public static List<Task> addNewTask(List<Task> existingTasks, Task newTask){
        // Create a new list for tasks to avoid altering the original list directly.
        List<Task> updatedTasks = new ArrayList<>(existingTasks);

        // Add the new task to the list
        updatedTasks.add(newTask);

        // Now, separate the tasks into unfinished and finished, similar to the reorder method.
        List<Task> unfinishedTasks = new ArrayList<>();
        List<Task> finishedTasks = new ArrayList<>();

        for (Task task : updatedTasks) {
            if (task.finished()) {
                finishedTasks.add(task);
            } else {
                unfinishedTasks.add(task);
            }
        }

        // Define the custom order for unfinished tasks
        Comparator<Task> tagOrderComparator = (task1, task2) -> {
            String order = "HWSE"; // Define the custom order
            return Integer.compare(order.indexOf(task1.tag()), order.indexOf(task2.tag()));
        };

        // Sort the unfinished tasks based on the predefined tag order
        unfinishedTasks.sort(tagOrderComparator);

        // Clear the updatedTasks list and repopulate it: unfinished tasks first, then finished ones
        updatedTasks.clear();
        updatedTasks.addAll(unfinishedTasks);
        updatedTasks.addAll(finishedTasks);

        // Assign new order based on their position in the updated list
        assignOrder(updatedTasks);

        // Return the newly ordered list
        return updatedTasks;
    }

    public static void assignOrder(List<Task> tasks) {
        for(int i = 0; i < tasks.size(); i++){
            tasks.get(i).setSortOrder(i);
        }
    }
}
