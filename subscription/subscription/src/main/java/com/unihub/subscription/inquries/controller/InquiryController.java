package com.unihub.subscription.inquries.controller;

import com.unihub.subscription.inquries.dto.request.InquiryRequestDto;
import com.unihub.subscription.inquries.dto.response.InquiresMetaData;
import com.unihub.subscription.inquries.dto.response.InquiryResponseDto;
import com.unihub.subscription.inquries.service.IInquiryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inquiries")
public class InquiryController {

    private final IInquiryService inquiryService;

    @PostMapping("/public/create")
    public ResponseEntity<InquiryResponseDto>createInquiry(@Valid @RequestBody InquiryRequestDto inquiry){
        return ResponseEntity.status(HttpStatus.CREATED).body(inquiryService.createInquiry(inquiry));
    }

    @DeleteMapping("/customer-service/delete-inquiry/{id}")
    public ResponseEntity<Void>deleteInquiry(@PathVariable UUID id){
        inquiryService.deleteInquiry(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer-service/get-inquiry/{id}")
    public ResponseEntity<InquiryResponseDto>getInquiry(@PathVariable UUID id){
        return ResponseEntity.ok(inquiryService.getInquiry(id));
    }

    @GetMapping("/customer-service/get-inquiries")
    public ResponseEntity<InquiresMetaData>getInquiry(@RequestParam(name = "page_num", defaultValue = "1") int pageNum,
                                                      @RequestParam(value = "sort_dir", defaultValue = "desc") String sortDir){
        return ResponseEntity.ok(inquiryService.getInquires(pageNum,sortDir));
    }


}

