package com.unihub.taskmanager.service.state.impl;

import com.unihub.taskmanager.model.Status;
import com.unihub.taskmanager.model.Task;
import com.unihub.taskmanager.service.state.TaskState;

import java.time.LocalDateTime;

public class FinishState implements TaskState {
    @Override
    public Task handle(Task task){
        checkState(task);
        task.setStatus(Status.DONE);
        task.setFinishedAt(LocalDateTime.now());
        return task;
    }

    private void checkState(Task task) {
        Status status = task.getStatus();
        if (status == Status.DONE) {
            throw new IllegalArgumentException("Task is already completed");
        }
        if (status == Status.TODO) {
            throw new IllegalArgumentException("Task must be in progress before completing");
        }
    }
}
