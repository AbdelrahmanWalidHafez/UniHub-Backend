package com.unihub.taskmanager.service.impl;

import com.unihub.taskmanager.dto.request.CreateTaskRequest;
import com.unihub.taskmanager.dto.response.TaskDto;
import com.unihub.taskmanager.mapper.TaskMapper;
import com.unihub.taskmanager.model.Status;
import com.unihub.taskmanager.model.Task;
import com.unihub.taskmanager.repository.TaskRepository;
import com.unihub.taskmanager.service.ITaskService;
import com.unihub.taskmanager.service.state.context.TaskStateContext;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements ITaskService {

    private final TaskMapper taskMapper;

    private final TaskRepository taskRepository;

    private final TaskStateContext context;

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

    @Override
    @Transactional
    public void deleteTasks(List<UUID> ids, HttpServletRequest request){
        List<Task> tasks=taskRepository.findAllByIdInAndCreatedBy(ids,fetchEmailFromHeader(request));
        taskRepository.deleteAllInBatch(tasks);
    }

    @Override
    public List<TaskDto> getTasks(HttpServletRequest request,int pageNum){
        return taskRepository
                .findByCreatedBy(fetchEmailFromHeader(request),generatePageable(pageNum))
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @Override
    public TaskDto setTaskStatus(UUID id, HttpServletRequest request, Status status){
        Task task=fetchTask(id,fetchEmailFromHeader(request));
        return  taskMapper.toDto(taskRepository.save(context.handleRequest(task,status)));
    }

    @Override
    public TaskDto editTask(UUID id,CreateTaskRequest taskRequest,HttpServletRequest request){
        Task task=fetchTask(id,fetchEmailFromHeader(request));
        editTask(task,taskRequest);
        return taskMapper.toDto(taskRepository.save(task));
    }

    private String fetchEmailFromHeader(HttpServletRequest request){
        return request.getHeader("X-User-Email");
    }

    private Task fetchTask(UUID id,String email){
        return taskRepository.findByIdAndCreatedBy(id,email)
                .orElseThrow(()-> new EntityNotFoundException("No Task found with id "+id));
    }

    private Pageable generatePageable(int pageNum){
        return PageRequest.of(pageNum-1,10, Sort.by("createdAt").descending());
    }

    private void editTask(Task task ,CreateTaskRequest taskRequest){
        task.setTitle( taskRequest.getTitle());
        task.setPriority(taskRequest.getPriority());
        task.setDescription(taskRequest.getDescription());
        task.setDueDate(taskRequest.getDueDate());
    }
}
