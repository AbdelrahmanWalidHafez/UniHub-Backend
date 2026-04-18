package com.unihub.ai.tool;

import com.unihub.ai.client.AuthFeignClient;
import com.unihub.ai.client.ClassRoomFeignClient;
import com.unihub.ai.client.UniversityFeignClient;
import com.unihub.ai.client.dto.CollegeDto;
import com.unihub.ai.client.dto.UniversityResponse;
import com.unihub.ai.client.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserTools {

    @Value("${api.key}")
    private String apiKey;

    private final AuthFeignClient authFeignClient;

    private final ClassRoomFeignClient classRoomFeignClient;

    private final UniversityFeignClient universityFeignClient;

    @Tool(name = "getUserInfo"
            ,description = """ 
            fetch user's info including general information such as the user's(name,dob,gender,role in the system),
             it also contains other information such as the university id (tid) and the college(cid) and gpa if the user is of role student .
              The user  maybe also an instructor i.
            """)
    UserDto getUserInfo(@ToolParam(description = "The current user's email .") String email){
        return authFeignClient.getUserInfo(email,apiKey).getBody();
    }

    @Tool(name = "getUserUniversity"
            ,description = """ 
            fetch user's University data.
            """)
    UniversityResponse getUserUniversity(@ToolParam(description = "The current user's university id") UUID id){
        return universityFeignClient.fetchUniversity(id).getBody();
    }

    @Tool(name = "getUserCollege"
            ,description = """ 
            fetch user's College name and campus.
            """)
    CollegeDto getUserCollege(@ToolParam(description = "The current user's college id") UUID id,@ToolParam(description = "The current user's university id") UUID tid){
        return  universityFeignClient.getCollege(id,tid).getBody();
    }

    @Tool(name = "getUserClassRoomsIds",description = """
               Returns all classroom IDs that the user belongs to.
              This includes classrooms where the user is either:
               - the instructor (owner)
               - or an enrolled student/member
             Use this to filter search results or restrict access to user-specific data.
            """)
    List<UUID> getUserClassRoomsIds(@ToolParam(description = "The current user's email") String email){
        return classRoomFeignClient.getClasses(email).getBody();
    }




}
