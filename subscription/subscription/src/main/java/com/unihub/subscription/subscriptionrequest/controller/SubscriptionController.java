package com.unihub.subscription.subscriptionrequest.controller;

import com.unihub.subscription.common.exception.dto.ErrorResponseDto;
import com.unihub.subscription.stripe.dto.response.SubscriptionPlanResponseDto;
import com.unihub.subscription.subscriptionrequest.dto.request.SubscriptionRequestDto;
import com.unihub.subscription.subscriptionrequest.dto.request.UpdateSubscriptionRequestDto;
import com.unihub.subscription.subscriptionrequest.dto.response.AfterUpdateResponse;
import com.unihub.subscription.subscriptionrequest.dto.response.SubscriptionRequestResponseDto;
import com.unihub.subscription.subscriptionrequest.dto.response.SubscriptionRequestResponses;
import com.unihub.subscription.subscriptionrequest.model.Status;
import com.unihub.subscription.subscriptionrequest.service.ISubscriptionRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(
        name = "Subscription request API",
        description = "APIs for managing subscription requests, including creation, retrieval, update, deletion"
)
public class SubscriptionController {

   
    private final ISubscriptionRequestService subscriptionRequestService;

    @Operation(
            summary = "creates a Subscription request",
            description = "Enables a Customer to request a subscription"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Subscription request created successfully",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = SubscriptionRequestResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "bad request",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponseDto.class))
            ),
    })
    @PostMapping(value="/public/request-subscription",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SubscriptionRequestResponseDto> requestSubscription(@Valid @RequestPart(value = "data") SubscriptionRequestDto subscriptionRequestDto
            , @RequestPart(value = "file") MultipartFile accreditation
            , @RequestPart(value = "media") MultipartFile logo)throws Exception{
        return ResponseEntity
                .status(HttpStatus.SC_CREATED)
                .body(subscriptionRequestService.createSubscriptionRequest(subscriptionRequestDto,accreditation,logo));
    }

    @Operation(
            summary = "fetch a Subscription request",
            description = "Enables a Customer service to fetch a subscription request by tis id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Subscription request fetched successfully",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = SubscriptionRequestResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "bad request",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "there is no subscription request with the same id",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponseDto.class))
            ),
    })
    @GetMapping("/customer-service/get-request/{id}")
    public ResponseEntity<SubscriptionRequestResponseDto> getSubscription(@PathVariable UUID id) throws IOException {
        return ResponseEntity.ok().body(subscriptionRequestService.getSubscription(id));
    }

    @Operation(
            summary = "fetch all the subscription requests",
            description = "Enables a Customer service user to fetch a subscription requests"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Subscription requests fetched successfully",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = SubscriptionRequestResponses.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "bad request",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponseDto.class))
            ),
    })
    @GetMapping("/customer-service/get-requests")
    public ResponseEntity<SubscriptionRequestResponses> getSubscriptionRequests(
            @RequestParam(name = "page_num", defaultValue = "1") int pageNum,
            @RequestParam(value = "sort_dir", defaultValue = "desc") String sortDir,
            @RequestParam(value = "sort_field", defaultValue = "createdAt") String sortField,
            @RequestParam(required = false) Status status){
        return ResponseEntity.ok(SubscriptionRequestResponses
                .builder()
                .metaDataList(subscriptionRequestService.getSubscriptionRequests(pageNum,sortDir,sortField,status))
                .build());
    }

    @Operation(
            summary = "deletes a Subscription request",
            description = "Enables a Customer service user to delete a subscription request"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Subscription plan deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "bad request",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "there is no subscription request with the same id",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponseDto.class))
            ),
    })
    @DeleteMapping("/customer-service/delete-request/{id}")
    public ResponseEntity<?> deleteSubscriptionRequests(@PathVariable UUID id) throws IOException {
        subscriptionRequestService.deleteSubscriptionRequest(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "updates a Subscription request",
            description = "Enables a Customer service user to update a subscription request"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Subscription request updated successfully",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = AfterUpdateResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "bad request",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "there is no subscription request with the same id",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponseDto.class))
            ),
    })
    @PatchMapping("/customer-service/update-request-status/{id}")
    public ResponseEntity<AfterUpdateResponse> getSubscriptionRequests(@Valid @RequestBody UpdateSubscriptionRequestDto updateSubscriptionRequestDto, @PathVariable UUID id){
        return ResponseEntity.ok(subscriptionRequestService.updateSubscriptionStatus(updateSubscriptionRequestDto,id));
    }

}
