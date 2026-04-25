package com.unihub.taskmanager.mapper;

import com.unihub.taskmanager.dto.request.CreateTaskRequest;
import com.unihub.taskmanager.dto.response.TaskDto;
import com.unihub.taskmanager.model.Status;
import com.unihub.taskmanager.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    public Task toEntity(CreateTaskRequest request){
        Task task= new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());
        task.setStatus(Status.TODO);
        return task;
    }
    public TaskDto toDto(Task task){
        TaskDto taskDto= new TaskDto();
        taskDto.setDescription(task.getDescription());
        taskDto.setPriority(task.getPriority());
        taskDto.setStatus(task.getStatus());
        taskDto.setDueDate(task.getDueDate());
        taskDto.setId(task.getId());
        taskDto.setFinishedAt(task.getFinishedAt());
        taskDto.setStartedAt(task.getStartedAt());
        taskDto.setCreatedBy(task.getCreatedBy());
        taskDto.setCreatedAt(task.getCreatedAt());
        taskDto.setUpdatedAt(task.getUpdatedAt());
        taskDto.setUpdatedBy(task.getUpdatedBy());
        return taskDto;

    }



}
