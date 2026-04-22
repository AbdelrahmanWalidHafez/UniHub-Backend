package com.unihub.classroom.clazz.service.state.impl;

import com.unihub.classroom.clazz.model.ClassRoom;
import com.unihub.classroom.clazz.service.state.ClassRoomState;

public class ArchivedState implements ClassRoomState {

    @Override
    public void handleRequest(ClassRoom classRoom) {
        classRoom.setArchived(false);
    }
}
