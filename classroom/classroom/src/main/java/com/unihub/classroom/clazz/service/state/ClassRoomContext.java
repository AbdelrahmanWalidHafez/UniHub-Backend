package com.unihub.classroom.clazz.service.state;

import com.unihub.classroom.clazz.model.ClassRoom;
import com.unihub.classroom.clazz.service.state.impl.ArchivedState;
import com.unihub.classroom.clazz.service.state.impl.UnArchivedState;

public class ClassRoomContext {

    private ClassRoomState state;

    public void setState(ClassRoom classRoom) {
        if (classRoom.isArchived()) {
            state = new ArchivedState();
        }else {
            state = new UnArchivedState();
        }
    }

    public void handleRequest(ClassRoom classRoom) {
        state.handleRequest(classRoom);
    }
}
