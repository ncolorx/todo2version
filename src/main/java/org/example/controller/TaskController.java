package org.example.controller;

import org.example.model.Status;
import org.example.model.Task;
import org.example.service.TaskService;

import java.util.List;

public class TaskController {
    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    public void command(String console) {
        String[] parts = console.split(" ", 2);
        String command = parts[0].toLowerCase();
        switch (command) {
            case "list" -> {
                if (service.getAllTask() != null) {
                    service.getAllTask().forEach(task -> {
                        System.out.println("Задача: " + task.getTitle() + "|ID задачи: " + task.getId() + "|статус: " + task.getStatus() + "|Описание: " + task.getDescription() + "|Дедлайн: " + task.getDueDate());
                    });
                } else {
                    System.out.println("Список задач пуст! Добавьте задачу с помощью команды add и повторите попытку.");
                }
            }
            case "delete" -> {
                if (parts.length < 2) {
                    System.out.println("После delete, введите: id задачи, которую хотите удалить через пробел после самой команды delete.");
                    return;
                }
                try {
                    int id = Integer.parseInt(parts[1]);
                    if (!service.deleteTask(id)) {
                        System.out.println("Задач с таким id нет в списке или они уже были удалены!");
                    } else {
                        System.out.println("Задача была успешно удалена! Используйте команду list для проверки списка задач.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка! Введенный параметр не является числом, пожалуйста повторите попытку! Или выберете другую команду:");
                }
            }
            case "filter" -> {
                if (parts.length < 2) {
                    System.out.println("После команды filter необходимо через пробел ввести статус: Todo, InProgress, Done.");
                    return;
                }
                try {
                    Status status = Status.fromString(parts[1]);
                    if (service.filterByStatus(status).isEmpty()) {
                        System.out.println("Задач с таким статусом нет в списке или такого статуса не существует, поэтому фильтрация по статусу не была произведена.");
                        System.out.println("Пожалуйста добавьте задачу с помощью команды add и повторите попытку.");
                    } else {
                        List<Task> tasks = service.filterByStatus(status);
                        tasks.stream().forEach(task -> {
                            System.out.println("Задача: " + task.getTitle() + "ее статус: " + task.getStatus());
                        });
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Статус не является корректным или не существует. Вводить нужно в формате: Todo, InProgress, Done.");
                    System.out.println("Повторите команду filter снова или выберете другую команду.");
                    System.out.println("После ввода команды filter сделайте пробел и выберете правильный статус: Todo, InProgress, Done.");
                }
            }
            case "sort" -> {
                if (parts.length < 2) {
                    System.out.println("После команды sort, необходимо ввести по какому параметру будет сортировка: Дедлайн, Статус.");
                    System.out.println("Повторите попытку или выберете другую команду:");
                    return;
                }
                String[] args = parts[1].split(" ", 2);
                if (args.length < 1) {
                    System.out.println("После команды sort, необходимо ввести, почему будет сортировка: Дедлайн, Статус.");
                    System.out.println("Принимаемый вариант ввода: Дедлайн, Статус.");
                    return;
                }
                String sortValue = args[0];
                String sortValueToLowerCase = sortValue.toLowerCase();
                if (sortValueToLowerCase.equals("дедлайн")) {
                    if (service.sortedByDueDate().isEmpty()) {
                        System.out.println("Задач нет в списке, пожалуйста добавьте их с помощью команды add и повторите попытку.");
                    } else {
                        List<Task> tasks = service.sortedByDueDate();
                        tasks.stream().forEach(task -> {
                            System.out.println("Задача: " + task.getTitle() + "Дедлайн: " + task.getDueDate());
                        });
                    }
                } else if (sortValueToLowerCase.equals("статус")) {
                    if (args.length < 2) {
                        System.out.println("Необходимо ввести по какому из статусов хотите сделать сортировку, возможные варианты ввода: Todo, InProgress,Done.");
                        System.out.println("Повторите попытку в виде: sort статус Todo, InProgress, Done.");
                        return;
                    }
                    try {
                        Status status = Status.fromString(args[1]);
                        if (service.sortedByStatus(status).isEmpty()) {
                            System.out.println("Список задач пуст! Или задач с таким статусом еще не добавлено! Пожалуйста используйте команду add и повторите попытку!");
                        } else {
                            List<Task> tasks = service.filterByStatus(status);
                            tasks.stream().forEach(task -> {
                                System.out.println("Задача: " + task.getTitle() + "статус: " + task.getStatus());
                            });
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Статус не является корректным или не существует. Возможные варианты для ввода: Todo, InProgress, Done.");
                    }
                } else {
                    System.out.println("Неверный параметр сортировки, необходимо ввести: Дедлайн, Статус.");
                }
            }
            case "field" -> {
                if (parts.length < 2) {
                    System.out.println("После команды field необходимо ввести id задачи, у которой хотите получить поле.");
                    System.out.println("Повторите попытку или выберете другую команду: ");
                    return;
                }
                String[] args = parts[1].split(" ", 2);
                String id = args[0];
                if (args.length < 2) {
                    System.out.println("Нужно ввести и id и поле. Полный пример команды: field 1 Статус");
                    return;
                }
                try {
                    int fieldId = Integer.parseInt(id);
                    Object fieldValue = service.getFieldTaskById(fieldId, args[1]);
                    if (fieldValue != null) {
                        System.out.println(fieldValue);
                    } else {
                        System.out.println("Такого поля не существует или задача с таким id не найдена! Повторите попытку!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Необходимо после ввода команды field ввести строку именно в числовом формате, например: 12");
                }
            }
            default -> System.out.println("Такой команды не существует или она написана неправильно: " + command);
        }
    }
}
