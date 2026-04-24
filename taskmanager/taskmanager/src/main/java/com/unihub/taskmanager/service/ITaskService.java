package com.unihub.taskmanager.service;

import com.unihub.taskmanager.dto.request.CreateTaskRequest;
import com.unihub.taskmanager.dto.response.TaskDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public interface ITaskService {

    TaskDto createTask(CreateTaskRequest createTaskRequest) ;

    TaskDto getTask(UUID id, HttpServletRequest request);

    void deleteTask(UUID id, HttpServletRequest request);

}
