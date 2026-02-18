package com.unihub.s3.controller;

import com.unihub.s3.common.dto.ErrorResponseDto;
import com.unihub.s3.dto.response.FileResponse;
import com.unihub.s3.service.IS3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
@Tag(
        name = "S3 API",
        description = "API for fetching /downloading a file from amazon s3 bucket"
)
public class S3Controller {

    private final IS3Service s3Service;

    @Operation(
            summary = "fetches an s3 object",
            description = "Enables a client application to fetch an object from amazon s3 bucket"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "object fetched successfully successfully",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = byte.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "there is no S3 object with the same key",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "bad request",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    @GetMapping("/get-file/{key}")
    public ResponseEntity<byte[]> getAccreditation(@PathVariable String key) {
        FileResponse file = s3Service.downloadFile(key);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.inline().filename(key).build().toString())
                .body(file.getContent());
    }

}
