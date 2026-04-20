package com.unihub.ai.tool;

import com.unihub.ai.client.AuthFeignClient;
import com.unihub.ai.client.ClassRoomFeignClient;
import com.unihub.ai.client.UniversityFeignClient;
import com.unihub.ai.client.dto.CollegeDto;
import com.unihub.ai.client.dto.UniversityResponse;
import com.unihub.ai.client.dto.UserDto;
import com.unihub.ai.tool.concerns.UserToolsConcerns;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Tool(name = UserToolsConcerns.GET_USER_TOOL_NAME,description = UserToolsConcerns.GET_USER_TOOL_DESCRIPTION)
    UserDto getUserInfo(@ToolParam(description =UserToolsConcerns.USER_EMAIL_PARAM) String email){
        return authFeignClient.getUserInfo(email,apiKey).getBody();
    }

    @Tool(name = UserToolsConcerns.GET_UNI_TOOL_NAME,description =UserToolsConcerns.GET_UNI_TOOL_DESCRIPTION)
    UniversityResponse getUserUniversity(@ToolParam(description = UserToolsConcerns.UNI_ID_PARAM) UUID id){
        return universityFeignClient.fetchUniversity(id).getBody();
    }

    @Tool(name = UserToolsConcerns.GET_COL_TOOL_NAME,description =UserToolsConcerns.GET_USER_TOOL_DESCRIPTION )
    CollegeDto getUserCollege(@ToolParam(description =UserToolsConcerns.COL_ID_PARAM) UUID id,@ToolParam(description = UserToolsConcerns.UNI_ID_PARAM) UUID tid){
        return  universityFeignClient.getCollege(id,tid).getBody();
    }

    @Tool(name = UserToolsConcerns.CLASS_TOOL_NAME,description =UserToolsConcerns.CLASS_TOOL_DESCRIPTION)
    List<UUID> getUserClassRoomsIds(@ToolParam(description = UserToolsConcerns.USER_EMAIL_PARAM) String email){
        return classRoomFeignClient.getClasses(email).getBody();
    }
}
