package com.unihub.ai.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZoneId;

@Component
public class TimeTools {

    private static final Logger logger= LoggerFactory.getLogger(TimeTools.class);

    @Tool(name = "getCurrentLocalTime",description = "get the current time in the user's time zone")
    String getCurrentLocalTime(){
        logger.info("getCurrentLocalTime");
        return LocalTime.now().toString();
    }

    @Tool(name = "getCurrentTime",description = "get the current time in the specified time zone")
    String getCurrentTime(@ToolParam(description = "value representing the time zone") String timeZone){
        logger.info("returning the time zone:"+timeZone);
        return LocalTime.now(ZoneId.of(timeZone)).toString();
    }
}