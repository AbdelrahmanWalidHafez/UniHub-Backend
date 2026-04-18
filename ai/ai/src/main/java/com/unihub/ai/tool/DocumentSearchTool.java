package com.unihub.ai.tool;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DocumentSearchTool {

    private final VectorStore vectorStore;

    @Tool(name = "searchClassroomDocuments", description = """
            Searches the user's course materials (lecture notes, PDFs, slides, etc.) for content relevant to a given query.
            Only call this tool when the user asks an academic question that could be answered from their course materials
            (e.g., explaining a concept, summarizing a topic, reviewing lecture content).
            You MUST call getUserClassRoomsIds first to get the classroom IDs before calling this tool.
            Do NOT call this tool for general knowledge questions, greetings, or questions unrelated to course content.
            """)
    public String searchClassroomDocuments(
            @ToolParam(description = "The search query derived from the user's question") String query,
            @ToolParam(description = "List of classroom UUIDs to restrict document search to (obtained from getUserClassRoomsIds)") List<UUID> classroomIds
    ) {
        if (classroomIds == null || classroomIds.isEmpty()) {
            return "No classroom IDs provided — cannot search documents.";
        }

        FilterExpressionBuilder b = new FilterExpressionBuilder();
        var filterExpression = classroomIds.stream()
                .map(id -> b.eq("classroomId", id.toString()))
                .reduce(b::or)
                .map(FilterExpressionBuilder.Op::build)
                .orElse(null);

        SearchRequest request = SearchRequest.builder()
                .query(query)
                .topK(8)
                .similarityThreshold(0.3)
                .filterExpression(filterExpression)
                .build();

        List<Document> results = vectorStore.similaritySearch(request);

        if (results.isEmpty()) {
            return "No relevant course materials found for this query.";
        }

        return results.stream()
                .map(Document::getFormattedContent)
                .collect(Collectors.joining("\n\n---\n\n"));
    }
}
