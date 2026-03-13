package com.unihub.ai.tool;

import com.unihub.ai.client.AuthFeignClient;
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

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserTools {

    @Value("${api.key}")
    private String apiKey;

    private final UniversityFeignClient universityFeignClient;

    private final AuthFeignClient authFeignClient;


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




}
