package com.unihub.ai.tool;

import com.unihub.ai.tool.concerns.DocumentSearchToolConcerns;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DocumentSearchTool {

    private final VectorStore vectorStore;

    public static final String CLASS_IDS_NULL_MSG = "No classroom IDs provided — cannot search documents.";

    public static final String NO_RELEVANT_COURSES_MSG = "No relevant course materials found for this query.";

    public static final String NO_CONTENT_MSG = "No content found for this material. It may not have been indexed yet.";

    @Tool(name = DocumentSearchToolConcerns.TOOL_NAME, description = DocumentSearchToolConcerns.TOOL_DESCRIPTION)
    public String searchClassroomDocuments(
            @ToolParam(description = DocumentSearchToolConcerns.TOOL_PARAM_QUERY) String query,
            @ToolParam(description = DocumentSearchToolConcerns.TOOL_PARAM_CLASSROOM_IDS) List<UUID> classroomIds,
            @ToolParam(description = DocumentSearchToolConcerns.TOOL_PARAM_MATERIAL_ID) UUID materialId
    ) {
        if (classroomIds == null || classroomIds.isEmpty()) {
            return CLASS_IDS_NULL_MSG;
        }
        FilterExpressionBuilder b = new FilterExpressionBuilder();
        Filter.Expression classroomFilter = classroomIds.stream()
                .map(id -> b.eq("classroomId", id.toString()).build())
                .reduce((left, right) -> new Filter.Expression(Filter.ExpressionType.OR, left, right))
                .orElse(null);
        Filter.Expression finalFilter;
        if (materialId != null) {
            Filter.Expression materialFilter = b.eq("materialId", materialId.toString()).build();
            finalFilter = new Filter.Expression(Filter.ExpressionType.AND, classroomFilter, materialFilter);
        }
        else {
            finalFilter = classroomFilter;
        }
        SearchRequest request = SearchRequest.builder()
                .query(query)
                .topK(materialId != null ? 12 : 8)
                .similarityThreshold(materialId != null ? 0.0 : 0.3)
                .filterExpression(finalFilter)
                .build();
        List<Document> results = vectorStore.similaritySearch(request);

        if (results.isEmpty()) {
            return materialId != null
                    ? NO_CONTENT_MSG
                    : NO_RELEVANT_COURSES_MSG;
        }
        return results.stream()
                .map(Document::getFormattedContent)
                .collect(Collectors.joining("\n\n---\n\n"));
    }
}
