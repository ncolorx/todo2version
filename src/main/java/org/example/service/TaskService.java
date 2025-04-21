package org.example.service;

import org.example.model.Status;
import org.example.model.Task;
import org.example.repository.TaskRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    //Добавление задачи в список
    public Task createTask(String title, String description, LocalDateTime dueDate) {
        if (title == null) {
            return null;
        }
        Task task = new Task(title, description, dueDate);
        repository.addTask(task);
        return task;
    }

    //Удаление задачи.
    public boolean deleteTask(int id) {
        Optional<Task> taskOptional = repository.findById(id);
        if (taskOptional.isEmpty()) {
            return false;
        }
        Task task = taskOptional.get();
        repository.deleteTaskForList(task.getId());
        return true;
    }

    //Метод для просмотра полей задачи.
    public Object getFieldTaskById(int id, String field) {
        Optional<Task> taskOptional = repository.findById(id);
        if (taskOptional.isEmpty()) {
            return null;
        } else {
            Task task = taskOptional.get();
            if (field == null) {
                return null;
            }
            switch (field.toLowerCase()) {
                case "название" -> {
                    return task.getTitle();
                }
                case "описание" -> {
                    return task.getDescription();
                }
                case "дедлайн" -> {
                    return task.getDueDate();
                }
                case "статус" -> {
                    return task.getStatus();
                }
                default -> {
                    return null;
                }
            }
        }
    }

    //Метод для редактирования задачи.
    public boolean editFieldTaskById(int id, String field, String setValueField) {
        Optional<Task> taskOptional = repository.findById(id);
        if (taskOptional.isEmpty()) {
            return false;
        }
        if (field == null || setValueField == null) {
            return false;
        }
        String fieldLowerCase = field.toLowerCase();
        if (fieldLowerCase.equals("статус") || fieldLowerCase.equals("дедлайн") || fieldLowerCase.equals("описание") || fieldLowerCase.equals("название")) {
            if (fieldLowerCase.equals("дедлайн")) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
                    LocalDateTime actualDate = LocalDateTime.parse(setValueField, formatter);
                    repository.updateField(id, field, actualDate.format(formatter));
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
            repository.updateField(id, field, setValueField);
            return true;
        } else {
            return false;
        }
    }

    //Метод для фильтра задачи по статусу:Сделанная/В процессе/Не начатая.
    public List<Task> filterByStatus(Status status) {
        if (repository.validStatus(status)) {
            return Collections.emptyList();
        }
        return repository.filterByStatus(status);
    }

    //Метод для сортировки задачи по статусу.
    public List<Task> sortedByStatus(Status status) {
        if (!repository.validStatus(status)) {
            return Collections.emptyList();
        }
        return repository.sortedByStatus(status);
    }

    //Метод для сортировки задачи по дедлайну.
    public List<Task> sortedByDueDate() {
        return repository.sortedByDueDate();
    }

    //Метод для получения всех задач.
    public List<Task> getAllTask() {
        return repository.getAllTasks();
    }
}





