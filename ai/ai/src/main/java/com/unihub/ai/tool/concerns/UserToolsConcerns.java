package com.unihub.ai.tool.concerns;

public interface UserToolsConcerns {

    String GET_USER_TOOL_NAME="getUserInfo";
    String GET_USER_TOOL_DESCRIPTION=""" 
            fetch user's info including general information such as the user's(name,dob,gender,role in the system),
             it also contains other information such as the university id (tid) and the college(cid) and gpa if the user is of role student .
              The user  maybe also an instructor i.
            """;
    String USER_EMAIL_PARAM="The current user's email .";
    String GET_UNI_TOOL_NAME="getUserUniversity";
    String GET_UNI_TOOL_DESCRIPTION= """ 
            fetch user's University data.
            """;
    String UNI_ID_PARAM="The current user's university id .";
    String GET_COL_TOOL_NAME="getUserCollege";
    String GET_COL_TOOL_DESCRIPTION="""
            fetch user's College name and campus.
            """;
    String COL_ID_PARAM="The current user's college id";
    String CLASS_TOOL_NAME="getUserClassRoomsIds";
    String CLASS_TOOL_DESCRIPTION= """
               Returns all classroom IDs that the user belongs to.
              This includes classrooms where the user is either:
               - the instructor (owner)
               - or an enrolled student/member
             Use this to filter search results or restrict access to user-specific data.
            """;
}
