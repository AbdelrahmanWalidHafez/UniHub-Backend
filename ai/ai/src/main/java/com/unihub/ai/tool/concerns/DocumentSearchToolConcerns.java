package com.unihub.ai.tool.concerns;

public interface DocumentSearchToolConcerns {
    String TOOL_NAME="searchClassroomDocuments";
    String TOOL_DESCRIPTION="""
        Searches the user's course materials (lecture notes, PDFs, slides, etc.) for content relevant to a given query.
        Only call this tool when the user asks an academic question that could be answered from their course materials.
        You MUST call getUserClassRoomsIds first to get the classroom IDs before calling this tool.
        When a materialId is provided, the search is scoped strictly to that single material; pass null if not scoped.
        Do NOT call this tool for general knowledge questions, greetings, or questions unrelated to course content.
        """;
    String TOOL_PARAM_QUERY="The search query derived from the user's question";
    String TOOL_PARAM_CLASSROOM_IDS="List of classroom UUIDs to restrict document search to (obtained from getUserClassRoomsIds)";
    String TOOL_PARAM_MATERIAL_ID="Optional: a single material UUID to restrict search to one specific material. Pass null for general academic questions.";
}
