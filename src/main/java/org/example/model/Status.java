package org.example.model;


import java.util.Optional;

public enum Status  {
    Todo(1),
    InProgress(2),
    Done(3);

    private final int priority;

    Status (int priority){
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }


    // Может быть стоит лучше использовать Optional? Ибо работаем со стримами потом и как бы предотвращаем null в стримах?
    // Да и вообще.
    public static Status fromString(String input){
       return switch (input.toLowerCase()){

            case "todo"  -> Todo;

            case "inprogress", "in_progress" -> InProgress;

            case "done" -> Done;


            default -> throw new IllegalArgumentException("Введеный статус неизвестен: " + input);


        };

    }


}

