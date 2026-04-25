package com.unihub.taskmanager.controller;

import com.unihub.taskmanager.dto.request.CreateTaskRequest;
import com.unihub.taskmanager.dto.response.TaskDto;
import com.unihub.taskmanager.dto.response.TasksDto;
import com.unihub.taskmanager.model.Status;
import com.unihub.taskmanager.service.ITaskService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final ITaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<TaskDto>createTask(@Valid @RequestBody CreateTaskRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(request));
    }

    @GetMapping("/get-task/{id}")
    public ResponseEntity<TaskDto>getTask(@PathVariable("id") UUID id, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.getTask(id,request));
    }

    @DeleteMapping("/delete-task/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable("id") UUID id, HttpServletRequest request){
        taskService.deleteTask(id,request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-in-batch")
    public ResponseEntity<?> deleteTasks(@Valid @RequestBody List<UUID> ids, HttpServletRequest request){
        taskService.deleteTasks(ids,request);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/get-tasks")
    public ResponseEntity<TasksDto> getTasks(HttpServletRequest request, @RequestParam(value = "page_num",defaultValue = "1")int pageNum){
        return ResponseEntity.ok(TasksDto.builder().tasks(taskService.getTasks(request,pageNum)).build());
    }

    @PutMapping("/edit-task/{id}")
    public ResponseEntity<TaskDto> editTask(@PathVariable("id") UUID id
            ,@Valid @RequestBody CreateTaskRequest createTaskRequest
            ,HttpServletRequest request){
        return ResponseEntity.ok(taskService.editTask(id,createTaskRequest,request));
    }

    @PatchMapping("/set-task-state/{id}")
    public ResponseEntity<TaskDto> setTaskStatus(@PathVariable("id") UUID id, HttpServletRequest request, @Valid @RequestBody Status status){
        return ResponseEntity.ok(taskService.setTaskStatus(id,request,status));
    }

}

