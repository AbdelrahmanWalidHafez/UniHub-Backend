package com.unihub.taskmanager.service.state.impl;

import com.unihub.taskmanager.model.Status;
import com.unihub.taskmanager.model.Task;
import com.unihub.taskmanager.service.state.TaskState;

public class IdleState implements TaskState {

    @Override
    public Task handle(Task task){
        task.setStatus(Status.TODO);
        task.setStartedAt((null));
        task.setFinishedAt(null);
        return  task;
    }

    private void checkState(Task task) {
        Status status = task.getStatus();
        if (status == Status.TODO) {
            throw new IllegalArgumentException("Task is already TODO");
        }
    }
}
