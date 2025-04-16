package org.example.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter


public class Task {
    private String title;
    private String description;
    private Status status;
    private LocalDateTime dueDate;
    private int id;

    public Task(String title, String description, LocalDateTime dueDate) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = Status.Todo;
    }

}

