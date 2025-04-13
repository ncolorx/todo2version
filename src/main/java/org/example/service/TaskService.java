package org.example.service;

import org.example.model.Status;
import org.example.model.Task;
import org.example.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository){
        this.repository = repository;
    }

    //Добавление задачи в список
    public void createTask(String title, String description, LocalDateTime dueDate){
        if(title == null){
            System.out.println("Укажите название задачи, оно не может быть пустым!");
            return;
        }
        Task task = new Task(title, description, dueDate);
        repository.addTask(task);
        System.out.println("Вы успешно добавили задачу!");
    }



    //Удаление задачи.
    public void deleteTask(int id){
        Optional<Task> taskOptional = repository.findById(id);
        if(taskOptional.isEmpty()){
            System.out.println("Задачи с таким id: " + id + " нет в списке!");
            return;
        }
        Task task = taskOptional.get();
        repository.deleteTaskForList(task.getId());
        System.out.println("Задача с id: " + id + " была успешно удалена!");
    }

    //Метод для просмотра полей задачи.
    public void getFieldTaskById(int id, String field){
        Optional<Task> taskOptional = repository.findById(id);
        if (taskOptional.isEmpty()){
            System.out.println("Задачи с таким id: " + id + " не существует!");
            return;
        }
        Task task = taskOptional.get();
        switch (field.toLowerCase()){
            case "Название" -> System.out.println(task.getTitle());
            case "Описание" -> System.out.println(task.getDescription());
            case "Дедлайн" -> System.out.println(task.getDueDate());
            case "Статус" -> System.out.println(task.getStatus());
            default -> System.out.println("Такого поля не существует!");
        }
    }

    //Метод для редактирования задачи.
    public void editFieldTaskById(int id, String field, String setValueField){
        Optional <Task> taskOptional = repository.findById(id);
        if(taskOptional.isEmpty()){
            System.out.println("Задачи с таким id: " + id + " не существует!");
            return;
        }
        repository.updateField(id, field, setValueField);

        }


        //Метод для фильтра задачи по статусу:Сделанная/В процессе/Не начатая.
    public void filterByStatus(Status status){
        List <Task> result = repository.filterByStatus(status);
        if(result.isEmpty()){
            System.out.println("Задач с таким статусом нет");
        }
        else {
            repository.displayTasks(result);
        }
    }

    //Метод для сортировки задачи по статусу.

    public void sortedByStatus(){
        List <Task> result = repository.sortedByStatus();
        if(result.isEmpty()){
            System.out.println("Задач с таким статусом нет");
        }
        else {
            repository.displayTasks(result);
        }
    }

    //Метод для сортировки задачи по дедлайну.

    public void sortedByDueDate(){
        List <Task> result = repository.sortedByDueDate();
        repository.displayTasks(result);

    }

    //Метод для вывода всех задач на экран.

    public void getAllTask(){
        List <Task> allTask = repository.getAll();
        if(allTask.isEmpty()){
            System.out.println("Список задач пуст!");
        }
        repository.displayTasks(allTask);
    }





    }





