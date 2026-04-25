package com.unihub.taskmanager.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TasksDto {

   private List<TaskDto> tasks;
}
