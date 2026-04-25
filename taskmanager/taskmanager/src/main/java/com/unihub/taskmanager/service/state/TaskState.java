package com.unihub.taskmanager.service.state;

import com.unihub.taskmanager.model.Task;

public interface TaskState {

    Task handle(Task task);
}
