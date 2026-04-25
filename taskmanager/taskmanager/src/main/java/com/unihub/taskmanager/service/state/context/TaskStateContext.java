package com.unihub.taskmanager.service.state.context;

import com.unihub.taskmanager.model.Status;
import com.unihub.taskmanager.model.Task;
import com.unihub.taskmanager.service.state.TaskState;
import com.unihub.taskmanager.service.state.impl.FinishState;
import com.unihub.taskmanager.service.state.impl.IdleState;
import com.unihub.taskmanager.service.state.impl.StartState;
import org.springframework.stereotype.Component;

@Component
public class TaskStateContext {

    private TaskState taskState;

    public Task handleRequest(Task task,Status status){
        initializeContext(status);
        return  taskState.handle(task);
    }

    private void initializeContext(Status status) {
        switch (status) {
            case DONE -> setState(new FinishState());
            case INPROGRESS -> setState(new StartState());
            default -> setState(new IdleState());
        }
    }
        private void setState(TaskState state){
        taskState=state;
    }
}
