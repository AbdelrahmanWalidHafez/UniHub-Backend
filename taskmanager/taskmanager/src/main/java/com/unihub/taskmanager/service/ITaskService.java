package com.unihub.taskmanager.service;

import com.unihub.taskmanager.dto.request.CreateTaskRequest;
import com.unihub.taskmanager.dto.response.TaskDto;
import com.unihub.taskmanager.model.Status;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

public interface ITaskService {

    TaskDto createTask(CreateTaskRequest createTaskRequest) ;

    TaskDto getTask(UUID id, HttpServletRequest request);

    void deleteTask(UUID id, HttpServletRequest request);

    void deleteTasks(List<UUID> ids, HttpServletRequest request);

    List<TaskDto> getTasks(HttpServletRequest request,int pageNum);

    TaskDto editTask(UUID id,CreateTaskRequest taskRequest,HttpServletRequest request);

    TaskDto setTaskStatus(UUID id, HttpServletRequest request, Status status);
}
