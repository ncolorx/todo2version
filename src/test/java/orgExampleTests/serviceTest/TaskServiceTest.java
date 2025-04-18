package orgExampleTests.serviceTest;

import org.example.model.Status;
import org.example.model.Task;
import org.example.repository.TaskRepository;
import org.example.service.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class TaskServiceTest {
    TaskRepository taskRepository = new TaskRepository();
    TaskService taskService = new TaskService(taskRepository);



    @Test
    void createTaskTest(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        LocalDateTime actualDate = LocalDateTime.parse("1212.12.12 12:12", formatter);
        Task created = taskService.createTask("Название", "Описание", actualDate);
        Assertions.assertNotNull(created);
        Assertions.assertEquals(1, taskRepository.getAllTasks().size());
        Assertions.assertEquals("Название", created.getTitle());
        LocalDateTime expectedDate = LocalDateTime.parse("1212.12.12 12:12", formatter);
        Assertions.assertEquals(expectedDate, created.getDueDate());
        Assertions.assertEquals("Описание", created.getDescription());
    }

    @Test
    void deleteTaskTest(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        LocalDateTime actualDate = LocalDateTime.parse("1212.12.12 12:12", formatter);
        Task created = taskService.createTask("Название", "Описание", actualDate);
        Assertions.assertTrue(taskService.deleteTask(created.getId()));
        boolean result = taskService.deleteTask(999);
        Assertions.assertFalse(result);
        Assertions.assertEquals(0, taskRepository.getAllTasks().size());
    }

    @Test
    void getFieldTaskByIdTest(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        LocalDateTime actualDate = LocalDateTime.parse("1212.12.12 12:12", formatter);
        Task created = taskService.createTask("Название", "Описание", actualDate);
        Object result = taskService.getFieldTaskById(999, "название");
        Assertions.assertNull(result);
        Object otherResult = taskService.getFieldTaskById(created.getId(), null);
        Assertions.assertNull(otherResult);
    }

    @Test
    void editFieldTaskByIdTest(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        LocalDateTime actualDate = LocalDateTime.parse("1212.12.12 12:12", formatter);
        Task created = taskService.createTask("Название", "Описание", actualDate);
        boolean result = taskService.editFieldTaskById(999, "Описание", "Название");
        Assertions.assertFalse(result);
        Assertions.assertFalse(taskService.editFieldTaskById(created.getId(), null, "bla-bla" ));
        Assertions.assertFalse(taskService.editFieldTaskById(created.getId(), "bla-bla", null));
        String fieldV1 = "название";
        String fieldV2 = "дедлайн";
        String fieldV3 = "описание";
        String fieldV4 = "статус";
        String format = actualDate.format(formatter);
        Assertions.assertTrue(taskService.editFieldTaskById(created.getId(), fieldV1, "bla-bla" ));
        Assertions.assertTrue(taskService.editFieldTaskById(created.getId(), fieldV2, format));
        Assertions.assertTrue(taskService.editFieldTaskById(created.getId(), fieldV3, "da-da"));
        Assertions.assertTrue(taskService.editFieldTaskById(created.getId(), fieldV4, "ja-va" ));

    }

    @Test
    void filterByStatusTest(){
        Status status2 = Status.fromString("Я не статус!");
        Assertions.assertEquals(Collections.emptyList(), taskService.filterByStatus(null));
        Assertions.assertEquals(Collections.emptyList(), taskService.filterByStatus(status2));
    }

    @Test
    void sortedByStatusTest(){
        Status status2 = Status.fromString("Я не статус!");
        Assertions.assertEquals(Collections.emptyList(), taskService.filterByStatus(null));
        Assertions.assertEquals(Collections.emptyList(), taskService.sortedByStatus(status2));
    }

    @Test
    void sortedByDueDateTest(){
        LocalDateTime date1 = LocalDateTime.of(2025, 4, 10, 12, 0); // раньше всех
        LocalDateTime date2 = LocalDateTime.of(2025, 4, 15, 12, 0);
        LocalDateTime date3 = LocalDateTime.of(2025, 4, 20, 12, 0); // позже всех

        Task task1 = taskService.createTask("Самая ранняя", "описание", date1);
        Task task2 = taskService.createTask("Средняя", "описание", date2);
        Task task3 = taskService.createTask("Поздняя", "описание", date3);


        List<Task> sortedTasks = taskService.sortedByDueDate();

        Assertions.assertEquals(3, sortedTasks.size());
        Assertions.assertEquals(task1.getId(), sortedTasks.get(0).getId()); // date1
        Assertions.assertEquals(task2.getId(), sortedTasks.get(1).getId()); // date2
        Assertions.assertEquals(task3.getId(), sortedTasks.get(2).getId()); // date3
    }

    @Test
    void getAllTaskTest(){
        List<Task> emptyList = taskService.getAllTask();
        Assertions.assertTrue(emptyList.isEmpty(), "Список должен быть пустым при старте");


        LocalDateTime now = LocalDateTime.now();
        Task task1 = taskService.createTask("задача 1", "описание", now);
        Task task2 = taskService.createTask("задача 2", "описание", now.plusDays(1));


        List<Task> result = taskService.getAllTask();

        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.contains(task1));
        Assertions.assertTrue(result.contains(task2));

    }

}
