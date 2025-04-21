package org.example.model;

import lombok.Getter;

@Getter
public enum Status {
    Todo(1),
    InProgress(2),
    Done(3);
    private final int priority;

    Status(int priority) {
        this.priority = priority;
    }

    public static Status fromString(String input) {
        if (input == null) {
            return null;
        }
        return switch (input.toLowerCase()) {
            case "todo" -> Todo;
            case "inprogress", "in_progress" -> InProgress;
            case "done" -> Done;
            default -> null;
        };
    }
}

