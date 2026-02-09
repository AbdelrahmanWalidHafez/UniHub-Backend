package com.unihub.subscription.inquries.service.impl;

import com.unihub.subscription.inquries.dto.request.InquiryRequestDto;
import com.unihub.subscription.inquries.dto.response.InquiresMetaData;
import com.unihub.subscription.inquries.dto.response.InquiryResponseDto;
import com.unihub.subscription.inquries.mapper.InquiryMapper;
import com.unihub.subscription.inquries.model.Inquiry;
import com.unihub.subscription.inquries.repository.InquiryRepository;
import com.unihub.subscription.inquries.service.IInquiryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IInquiryServiceImpl implements IInquiryService {

    private final InquiryRepository inquiryRepository;

    private final InquiryMapper inquiryMapper;

    @Override
    public InquiryResponseDto createInquiry(@Valid @RequestBody InquiryRequestDto inquiryRequestDto) {
        Inquiry inquiry=inquiryMapper.toEntity(inquiryRequestDto);
        Inquiry savedInquiry=inquiryRepository.save(inquiry);
        return inquiryMapper.toDto(savedInquiry);
    }

    @Override
    public InquiryResponseDto getInquiry(UUID id) {
        Inquiry inquiry=fetchInquiry(id);
        return inquiryMapper.toDto(inquiry);
    }

    @Override
    public void deleteInquiry(UUID id) {
        Inquiry inquiry=fetchInquiry(id);
        inquiryRepository.delete(inquiry);
    }

    @Override
    public InquiresMetaData getInquires(@RequestParam("page_num") int pageNum, @RequestParam("sort_dir") String sortDir) {
        Pageable pageable=createPageable(pageNum,sortDir);
        return InquiresMetaData.builder()
                .inquires(
                inquiryRepository.findAll(pageable).stream().map(inquiryMapper::toMetaData).toList()
        ).build();
    }

    private Inquiry fetchInquiry(UUID id) {
        return inquiryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Inquiry Not Found"));
    }
    private Pageable createPageable(int pageNum, String sortDir){
        int pageSize=10;
        return  PageRequest.of(
                pageNum-1,
                pageSize,
                sortDir.equalsIgnoreCase("asc")? Sort.by("createdAt").ascending():Sort.by("createdAt").descending()
        );
    }

}
