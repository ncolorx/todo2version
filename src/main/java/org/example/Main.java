package org.example;

import org.example.controller.TaskController;
import org.example.repository.TaskRepository;
import org.example.service.TaskService;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        TaskRepository repository = new TaskRepository();
        TaskService service = new TaskService(repository);
        TaskController controller = new TaskController(service);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Вас приветствует программа Todo-App");
        System.out.println("Введите команду (add, list, edit, delete, filter, sort, field, exit");

        while (true){
            String input = scanner.nextLine().trim();
            if("exit".equalsIgnoreCase(input)){
                System.out.println("Программа завершила работу!");
                break;
            }
            controller.command(input);

        }

        }
    }
