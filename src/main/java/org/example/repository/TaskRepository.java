package org.example.repository;


import org.example.model.Status;
import org.example.model.Task;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.example.model.Status.*;


public class TaskRepository{

    private final List  <Task> tasks = new ArrayList<>();
    private int newId = 0;


    //Метод для добавления задачи: учитывать название/статус/описание/двойную дату = время выполнения для задачи.

    public void addTask(Task task) {
        task.setId(newId++);
        tasks.add(task);
    }

    //Методы для редактирования задачи: 1) Для обновления статуса задачи. 2) Для изменения описания задачи.
    // 3) Для изменения срока выполнения задачи. 4) Для изменения названия задачи.

    public void updateField(int id, String field, String newValue){
        Optional <Task> taskOptional = findById(id);
        if(taskOptional.isEmpty()){
            System.out.println("Задача с таким: " + id + " не существует!");
            return;
        }
        Task task = taskOptional.get();

        switch (field.toLowerCase()){
            case "Название" -> task.setTitle(newValue);
            case "Описание" -> task.setDescription(newValue);
            case "Дедлайн" -> task.setDueDate(LocalDateTime.parse(newValue));
            case "Статус" -> {Status thisStatus = fromString(newValue);
                task.setStatus(thisStatus);
            }
        }

    }
    //Метод для проверки правильного статуса, введенного пользователем.
    public boolean statusIsValid(Status status){
        if(status == null){
            return  false;
        }
       return status.equals(Todo)  || status.equals(InProgress) || status.equals(Done);
    }



    //Метод для фильтра задач по статусу:Сделанные/В процессе выполнения/Не начатые.

    public List<Task> filterByStatus(Status status){
        if(!statusIsValid(status)){
            System.out.println("Ошибка! Переданный статус пустой или его не существует! " + status);
            return Collections.emptyList();
        }
        List<Task> ListFilterByStatus = tasks.stream().filter(task -> task.getStatus() == status).collect(Collectors.toList());
        return ListFilterByStatus;
    }


    // Методы для сортировки задачи по: 1) по статусу 2) по сроку выполнения.

    public List<Task> sortedByStatus(){
        List<Task> ListSortedByStatus = tasks.stream().sorted(
                Comparator.comparing(task -> task.getStatus().getPriority())
        ).collect(Collectors.toList());

        return  ListSortedByStatus;
    }

//Сортировка по дедлайну.
    public List <Task> sortedByDueDate(){
        List <Task> ListSortedByDueDate = tasks.stream().
                sorted(Comparator.comparing(Task -> Task.getDueDate()))
                .collect(Collectors.toList());
        return ListSortedByDueDate;
    }

    //Метод для удаления задачи
    public void deleteTaskForList(int id){
        Optional <Task> taskOptional = findById(id);
        if(taskOptional.isEmpty()){
            System.out.println("Задача с таким: " + id + "не найдена!");
            return;
        }
        Task task = taskOptional.get();
        for (int i = 0; i < tasks.size() ; i++) {
            if(tasks.get(i).getId() == task.getId()){
                tasks.remove(i);
            }
        }

    }


    //Метод для просмотра всех задач
    public void displayTasks(List <Task> tasks){
        for (Task task : tasks){
            System.out.println("Задача: " + task.getTitle() + " | id: " + task.getId());
        }
    }

    public Optional<Task> findById(int id){
       Optional<Task> find = tasks.stream().filter(s -> s.getId() == id).findFirst();
        return find;
    }

    public List<Task> getAll(){
        return new ArrayList<>(tasks);
    }



}
