package com.unihub.classroom.clazz.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassRoomsDto {

    @JsonProperty("class_rooms")
    private List<ClassRoomResponse> classRooms;

}
