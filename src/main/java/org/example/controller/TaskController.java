package org.example.controller;

import org.example.model.Status;
import org.example.service.TaskService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service){
        this.service = service;
    }

    public void command(String console){
        String parts [] = console.split(" ", 2);
        String command = parts[0].toLowerCase();

        switch (command){

            case "add" -> {

                System.out.println("Введите название задачи в порядке через пробел: Название, Дедлайн, Описание." );
                if (parts.length < 2){
                    System.out.println("Недостаточно аргументов, попробуйте еще раз.");
                    break;
                }
                String args [] = parts[1].split(" ", 3);
                if (args.length < 3) {
                    System.out.println("Недостаточно введенных параметров.");
                    break;
                }
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                   LocalDateTime deadline = LocalDateTime.parse(args[2], formatter);
                   service.createTask(args[0], args[1], deadline);
                }
                catch (Exception e){
                    System.out.println("Ошибка при вводе данных или даты: " + e.getMessage());
                }


            }

            case "list" -> {
                service.getAllTask();
            }

            case "edit" -> {
                System.out.println("Введите: id задачи, которую хотите редактировать, Название/Описание/Дедлайн/Статус, Текст для изменения");
                System.out.println("Вводить через пробел!");
                if(parts.length < 2){
                    System.out.println("Недостаточно аргументов, попробуйте еще раз. ");
                    break;
                }
                String args [] = parts[1].split(" ", 3);
                if(args.length < 3){
                    System.out.println("Недостаточно введенных параметров.");
                    break;

                }

                service.editFieldTaskById(Integer.parseInt(args[0]), args[1], args[2]);
            }

            case "delete" -> {
                System.out.println("Введите: id задачи, которую хотите удалить");
                service.deleteTask(Integer.parseInt(parts[1]));
            }

            case "filter" -> {
                System.out.println("Введите: статус, по которому хотите отфильтровать список задач.");
                System.out.println("Возможные варианты: Todo, InProgress, Done");
                service.filterByStatus(Status.fromString(parts[1]));

            }

            case "sort" -> {
                System.out.println("Введите: по чему будете сортировать: по дедлайну, по статусу.");
                System.out.println("Возможные варианты ввода: Дедлайн, Статус");
                if(parts[1].equalsIgnoreCase("Дедлайн")){
                    service.sortedByDueDate();
                }
                if(parts[1].equalsIgnoreCase("Статус")){
                    System.out.println("Введите по какому из статусов хотите отсортировать, возможные варианты: Todo, InProgress, Done");
                    String inputStatus = console;
                    service.sortedByStatus();
                }
                else {
                    System.out.println("Указан неправильный вариант ввода, попробуйте снова.");
                }

            }

            case "field" -> {
                System.out.println("Введите, id задачи и какое из полей хотите посмотреть у этой задачи, возможные варианты: Название/Описание/Дедлайн/Статус");
                System.out.println("Ввод id и поля осуществляется через пробел.");
                String args [] = parts[1].split(" ", 2);
                service.getFieldTaskById(Integer.parseInt(args[0]), args[1]);

            }

            case "exit" -> {
                System.out.println("Программа завершена!");
                return;

            }

            default -> System.out.println("Такой команды не существует или она написана неправильно: " + command);




        }
    }

}
