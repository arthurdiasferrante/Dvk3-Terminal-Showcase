package core_logic.models.system;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static core_logic.models.rules.Dvk3Config.*;

class Dvk3TaskManagerTest {

    @Test
    public void mustRemoveTaskByNameCorrectly() {
        Dvk3TaskManager taskManager = new Dvk3TaskManager();

        Dvk3TaskManager.Task task1 = new Dvk3TaskManager.Task("EXAMPLE_TASK_ONE", TIME_INFINITE);
        Dvk3TaskManager.Task task2 = new Dvk3TaskManager.Task("EXAMPLE_TASK_TWO", TIME_INFINITE);

        // adiciono as duas tarefas no meu taskmanager
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        // tento remover apenas a tarefa de exemplo número 2
        taskManager.killTaskByName("EXAMPLE_TASK_TWO");

        // o sistema deve me informar ter apenas 1 tarefa funcionando
        assertEquals(1, taskManager.getActiveTaskCounter(), "O contador deve ser 1");

        // o sistema não pode encontrar a tarefa EXAMPLE_TASK_TWO
        assertFalse(taskManager.getTasks().contains("EXAMPLE_TASK_TWO"), "A tarefa não se encontra no sistema");

        // o sistema deve conter a tarefa número 1 após o teste
        assertTrue(taskManager.getTasks().contains("EXAMPLE_TASK_ONE"), "A tarefa está em execução");
    }
}