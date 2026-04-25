package com.unihub.taskmanager.service.state.impl;

import com.unihub.taskmanager.model.Status;
import com.unihub.taskmanager.model.Task;
import com.unihub.taskmanager.service.state.TaskState;

import java.time.LocalDateTime;

public class StartState  implements TaskState {

    @Override
    public Task handle(Task task){
        task.setStatus(Status.INPROGRESS);
        task.setStartedAt(LocalDateTime.now());
        task.setFinishedAt(null);
        return  task;
    }

    private void checkState(Task task) {
        Status status = task.getStatus();
        if (status == Status.INPROGRESS) {
            throw new IllegalArgumentException("Task is already INPROGRESS");
        }
    }
}
