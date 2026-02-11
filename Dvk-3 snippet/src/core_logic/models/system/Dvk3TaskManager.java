package core_logic.models.system;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Dvk3TaskManager {
    private List<Task> activeTasks = new ArrayList<>();

    public void addTask(Task newTask) {
        activeTasks.add(newTask);
    }

    public void updateTasks() {
        // Apenas processa o tempo das tarefas
        Iterator<Task> it = activeTasks.iterator();
        while (it.hasNext()) {
            Task task = it.next();
            if (!task.processTask()) {
                it.remove();
            }
        }
    }

    public void killTaskByName(String name) {
        activeTasks.removeIf(task -> task.getTaskName().equals(name));
    }

    public void killAllTasks() {
        activeTasks.clear();
    }

    public int getActiveTaskCounter() {
        return Math.min(activeTasks.size(), 4);
    }

    public List<String> getTasks() {
        ArrayList<String> taskNames = new ArrayList<>();
        for (Task task : activeTasks) {
            taskNames.add(task.getTaskName());
        }
        return taskNames;
    }

    // --- Inner Class Task Simplificada ---

    public static class Task {
        private String taskName;
        private int taskDuration;

        public Task(String taskName, int taskDuration) {
            this.taskName = taskName;
            this.taskDuration = taskDuration;
        }

        public boolean processTask() {
            if (taskDuration > 0) {
                taskDuration--;
            } else if (taskDuration == 0) {
                return false; // Tarefa concluída
            }
            return true; // Tarefa ainda rodando
        }

        public String getTaskName() {
            return taskName;
        }
    }
}