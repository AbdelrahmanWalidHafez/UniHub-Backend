package com.unihub.taskmanager.controller;

import com.unihub.taskmanager.dto.request.CreateTaskRequest;
import com.unihub.taskmanager.dto.response.TaskDto;
import com.unihub.taskmanager.service.ITaskService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

//TODO-> get tasks,delete task in batch,start,end task,edit task
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
    public ResponseEntity deleteTask(@PathVariable("id") UUID id, HttpServletRequest request){
        taskService.deleteTask(id,request);
        return ResponseEntity.noContent().build();
    }

}

