package com.unihub.ai.tool;

import com.unihub.ai.tool.concerns.TimeToolsConcerns;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZoneId;

@Component
public class TimeTools {


    @Tool(name = TimeToolsConcerns.TIME_TOOL_NAME,description = TimeToolsConcerns.TIME_TOOL_DESCRIPTION)
    String getCurrentLocalTime(){
        return LocalTime.now().toString();
    }

    @Tool(name = TimeToolsConcerns.ZONE_TIME_TOOL_NAME,description = TimeToolsConcerns.ZONE_TIME_TOOL_DESCRIPTION)
    String getCurrentTime(@ToolParam(description = TimeToolsConcerns.TIME_ZONE_PARAM) String timeZone){
        return LocalTime.now(ZoneId.of(timeZone)).toString();
    }
}