package com.unihub.classroom.clazz.service.state;

import com.unihub.classroom.clazz.model.ClassRoom;

public interface ClassRoomState {

    void handleRequest(ClassRoom classRoom);
}
