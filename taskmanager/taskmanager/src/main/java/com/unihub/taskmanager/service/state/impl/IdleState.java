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
}
