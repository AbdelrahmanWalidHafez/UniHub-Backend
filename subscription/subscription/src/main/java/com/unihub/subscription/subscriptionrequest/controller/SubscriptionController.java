package com.unihub.subscription.subscriptionrequest.controller;

import com.unihub.subscription.subscriptionrequest.dto.request.SubscriptionRequestDto;
import com.unihub.subscription.subscriptionrequest.dto.request.UpdateSubscriptionRequestDto;
import com.unihub.subscription.subscriptionrequest.dto.response.AfterUpdateResponse;
import com.unihub.subscription.subscriptionrequest.dto.response.SubscriptionRequestResponseDto;
import com.unihub.subscription.subscriptionrequest.dto.response.SubscriptionRequestResponses;
import com.unihub.subscription.subscriptionrequest.model.Status;
import com.unihub.subscription.subscriptionrequest.service.ISubscriptionRequestService;
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

public class SubscriptionController {

   
    private final ISubscriptionRequestService subscriptionRequestService;

    @PostMapping(value="/public/request-subscription",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SubscriptionRequestResponseDto> requestSubscription(@Valid @RequestPart(value = "data") SubscriptionRequestDto subscriptionRequestDto
            , @RequestPart(value = "file") MultipartFile accreditation
            , @RequestPart(value = "media") MultipartFile logo)throws Exception{
        return ResponseEntity
                .status(HttpStatus.SC_CREATED)
                .body(subscriptionRequestService.createSubscriptionRequest(subscriptionRequestDto,accreditation,logo));
    }

    @GetMapping("/customer-service/get-request/{id}")
    public ResponseEntity<SubscriptionRequestResponseDto> getSubscriptionRequest(@PathVariable UUID id){
        return ResponseEntity.ok(subscriptionRequestService.getSubscription(id));
    }

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

    @DeleteMapping("/customer-service/delete-request/{id}")
    public ResponseEntity<?> deleteSubscriptionRequests(@PathVariable UUID id) throws IOException {
        subscriptionRequestService.deleteSubscriptionRequest(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/customer-service/update-request-status/{id}")
    public ResponseEntity<AfterUpdateResponse> getSubscriptionRequests(@Valid @RequestBody UpdateSubscriptionRequestDto updateSubscriptionRequestDto, @PathVariable UUID id){
        return ResponseEntity.ok(subscriptionRequestService.updateSubscriptionStatus(updateSubscriptionRequestDto,id));
    }

}
