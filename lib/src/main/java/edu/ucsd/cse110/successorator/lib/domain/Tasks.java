package edu.ucsd.cse110.successorator.lib.domain;

import java.util.ArrayList;
import java.util.List;

public class Tasks {
    public static List<Task> reorder(List<Task> tasks, Task target) {
        List<Task> newTask = new ArrayList<>();
        if(tasks.size() == 0){
            return tasks;
        }
        if(tasks.size() == 1){
            return tasks;
        }
        int location = -1;
        tasks.remove(target.sortOrder());
        if(tasks.size() == 1){
            if(!target.finished()){
                newTask.add(target);
                newTask.add(tasks.get(0));

            }else{
                if(tasks.get(0).finished()){
                    newTask.add(target);
                    newTask.add(tasks.get(0));
                }else{
                    newTask.add(tasks.get(0));
                    newTask.add(target);
                }


            }
            assignOrder(newTask);
            return newTask;
        }
        if(!target.finished()){
            newTask.add(target);
            for(int i = 0; i < tasks.size(); i++){
                newTask.add(tasks.get(i));
            }
        }else{
            for(int i = 0; i < tasks.size(); i++) {
                if(!tasks.get(i).finished()) {
                    newTask.add(tasks.get(i));
                    if(i == tasks.size()-1){
                        newTask.add(target);
                        assignOrder(newTask);
                        return newTask;
                    }
                }else{
                    if(i == tasks.size()-1){
                        newTask.add(target);
                        newTask.add(tasks.get(i));
                        assignOrder(newTask);
                        return newTask;
                    }
                    location = i;
                    newTask.add(target);
                    break;
                }
            }
            if(location == -1) {
                assignOrder(newTask);
                return newTask;
            }
            for(int i = location; i < tasks.size(); i++){
                newTask.add(tasks.get(i));
            }
        }



        assignOrder(newTask);
        return newTask;
    }

    public static List<Task> addNewTask(List<Task> tasks, Task target){
        List<Task> newTask = new ArrayList<>();
        int location = -1;
        if(tasks.size() == 0){
            newTask.add(target);
            target.setSortOrder(0);
            return newTask;
        }
        for(int i = 0; i < tasks.size(); i++) {
            if(!tasks.get(i).finished()) {
                newTask.add(tasks.get(i));
                if(i == tasks.size()-1){
                    newTask.add(target);
                    assignOrder(newTask);
                    return newTask;
                }
            }else{
                if(i == tasks.size()-1){
                    newTask.add(target);
                    newTask.add(tasks.get(i));
                    assignOrder(newTask);
                    return newTask;
                }
                location = i;
                newTask.add(target);
                break;
            }
        }
        if(location == -1) {
            assignOrder(newTask);
            return newTask;
        }
        for(int i = location; i < tasks.size(); i++){
            newTask.add(tasks.get(i));
        }

        assignOrder(newTask);

        return newTask;


    }

    public static void assignOrder(List<Task> tasks) {
        for(int i = 0; i < tasks.size(); i++){
            tasks.get(i).setSortOrder(i);
        }
    }
}
