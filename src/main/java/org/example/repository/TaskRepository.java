package org.example.repository;

import org.example.model.Status;
import org.example.model.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.example.model.Status.*;

public class TaskRepository {
    private final List<Task> tasks = new ArrayList<>();
    private int newId = 0;

    //Метод для добавления задачи: учитывать название/статус/описание/двойную дату = время выполнения для задачи.
    public void addTask(Task task) {
        task.setId(newId++);
        tasks.add(task);
    }

    //Методы для редактирования задачи: 1) Для обновления статуса задачи. 2) Для изменения описания задачи.
    // 3) Для изменения срока выполнения задачи. 4) Для изменения названия задачи.
    public void updateField(int id, String field, String newValue) {
        Task task = tasks.get(id);
        switch (field.toLowerCase()) {
            case "название" -> task.setTitle(newValue);
            case "описание" -> task.setDescription(newValue);
            case "дедлайн" -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
                LocalDateTime deadline = LocalDateTime.parse(newValue, formatter);
                try {
                    task.setDueDate(deadline);
                } catch (Exception ignored) {
                }
            }
            case "статус" -> {
                try {
                    Status thisStatus = fromString(newValue);
                    task.setStatus(thisStatus);
                } catch (Exception ignored) {
                }
            }
            default -> {
                System.out.println("Первое введенное поле не является: название/описание/дедлайн/статус!");
                System.out.println("Пожалуйста повторите попытку или выберете другую задачу!");
            }
        }
    }

    //Метод для проверки правильного статуса, введенного пользователем.
    public boolean validStatus(Status status) {
        if (status == null) {
            return false;
        }
        return status.equals(Todo) || status.equals(InProgress) || status.equals(Done);
    }

    //Метод для фильтра задач по статусу:Сделанные/В процессе выполнения/Не начатые.
    public List<Task> filterByStatus(Status status) {

        return tasks.stream().filter(task -> task.getStatus() == status).collect(Collectors.toList());
    }

    // Методы для сортировки задачи по: 1) по статусу 2) по сроку выполнения.
    public List<Task> sortedByStatus(Status status) {
        int currentPriority = status.getPriority();
        return tasks.stream().sorted(Comparator.comparingInt(task -> task.getStatus().getPriority() == currentPriority ? 0 : 1)).collect(Collectors.toList());
    }

    //Сортировка по дедлайну.
    public List<Task> sortedByDueDate() {
        return tasks.stream().
                sorted(Comparator.comparing(Task::getDueDate))
                .collect(Collectors.toList());
    }

    //Метод для удаления задачи
    public void deleteTaskForList(int id) {
        Optional<Task> taskOptional = findById(id);
        if (taskOptional.isEmpty()) {
            System.out.println("Задача с таким: " + id + "не найдена!");
            return;
        }
        Task task = taskOptional.get();
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == task.getId()) {
                tasks.remove(i);
            }
        }
    }

    //Метод для поиска задачи по айди и учета null.
    public Optional<Task> findById(int id) {
        return tasks.stream().filter(s -> s.getId() == id).findFirst();
    }

    //Метод для получения текущего списка задач, используется в других классах другого пакета.
    public List<Task> getAllTasks() {
        return tasks;
    }
}
