package org.example;

import org.example.controller.TaskController;
import org.example.repository.TaskRepository;
import org.example.service.TaskService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TaskRepository repository = new TaskRepository();
        TaskService service = new TaskService(repository);
        TaskController controller = new TaskController(service);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Вас приветствует программа Todo-App");
        while (true) {
            System.out.println("Введите команду (add, list, edit, delete, filter, sort, field, exit)");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("add")) {
                System.out.println("Введите название задачи");
                String title = scanner.nextLine().trim();
                System.out.println("Введите описание задачи");
                String description = scanner.nextLine().trim();
                System.out.println("Введите дату");
                String dueDate = scanner.nextLine().trim();
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
                    LocalDateTime deadline = LocalDateTime.parse(dueDate, formatter);
                    if (service.createTask(title, description, deadline) != null) {
                        System.out.println("Вы успешно добавили задачу!");
                    } else {
                        System.out.println("Название не может быть пустым, пожалуйста повторите попытку!");
                    }
                } catch (Exception e) {
                    System.out.println("Ошибка! Введен неверный формат даты, необходимо ввести: yyyy.MM.dd HH:mm");
                    System.out.println("Пример: 2005.12.12 12:12");
                }
            } else if (input.equalsIgnoreCase("edit")) {
                int idInt;
                while (true) {
                    System.out.println("Введите id задачи, которую хотите редактировать");
                    String id = scanner.nextLine().trim();
                    try {
                        idInt = Integer.parseInt(id);
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Необходимо ввести число, а не строку!");
                    }
                }
                String field;
                while (true) {
                    System.out.println("Введите поле, которое хотите поменять.");
                    System.out.println("Возможные варианты: название/описание/дедлайн/статус");
                    field = scanner.nextLine().trim().toLowerCase();
                    if (field.equals("название") || field.equals("описание") || field.equals("дедлайн") || field.equals("статус")) {
                        System.out.println("Введено корректное поле");
                        break;
                    } else {
                        System.out.println("Введено неверное поле, попробуйте снова.");
                    }
                }
                String newField;
                System.out.println("Введите само изменение поля.");
                System.out.println("Если вводите название/описание вводите в формате строки, то есть обычными буквами");
                System.out.println("Если вводите дедлайн, то вводите в формате yyyy.MM.dd HH:mm");
                System.out.println("Если вводите статус, вводите inProgress, todo, done");
                newField = scanner.nextLine().trim();
                try {
                    if (service.editFieldTaskById(idInt, field, newField)) {
                        System.out.println("Поле у задачи было изменено успешно, используете list для проверки изменения: ");
                    } else {
                        System.out.println("Задачи с таким id нет в списке! Повторите попытку!");
                    }
                } catch (Exception e) {
                    System.out.println("Введено неправильное поле даты, ее нужно вводить в формате: yyyy.MM.dd HH:mm");
                }
            } else if ("exit".equalsIgnoreCase(input)) {
                System.out.println("Программа завершила работу!");
                break;
            } else {
                controller.command(input);
            }
        }
    }
}
