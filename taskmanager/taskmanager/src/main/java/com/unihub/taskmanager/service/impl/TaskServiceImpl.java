package com.unihub.taskmanager.service.impl;

import com.unihub.taskmanager.dto.request.CreateTaskRequest;
import com.unihub.taskmanager.dto.response.TaskDto;
import com.unihub.taskmanager.mapper.TaskMapper;
import com.unihub.taskmanager.model.Task;
import com.unihub.taskmanager.repository.TaskRepository;
import com.unihub.taskmanager.service.ITaskService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements ITaskService {

    private final TaskMapper taskMapper;

    private final TaskRepository taskRepository;

    @Override
    public TaskDto createTask(CreateTaskRequest createTaskRequest){
        Task task = taskMapper.toEntity(createTaskRequest);
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    public TaskDto getTask(UUID id, HttpServletRequest request){
        return  taskMapper.toDto(fetchTask(id,fetchEmailFromHeader(request)));
    }

    @Override
    public void deleteTask(UUID id, HttpServletRequest request){
        Task task=fetchTask(id,fetchEmailFromHeader(request));
        taskRepository.delete(task);
    }


    private String fetchEmailFromHeader(HttpServletRequest request){
        return request.getHeader("X-User-Email");
    }

    private Task fetchTask(UUID id,String email){
        return taskRepository.findByIdAndCreatedBy(id,email)
                .orElseThrow(()-> new EntityNotFoundException("No Task found with id "+id));
    }

}
