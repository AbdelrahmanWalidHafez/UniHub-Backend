package com.unihub.subscription.inquries.service;


import com.unihub.subscription.inquries.dto.request.InquiryRequestDto;
import com.unihub.subscription.inquries.dto.response.InquiresMetaData;
import com.unihub.subscription.inquries.dto.response.InquiryResponseDto;

import java.util.UUID;

public interface IInquiryService {

    InquiryResponseDto createInquiry(InquiryRequestDto inquiryRequestDto);

    InquiryResponseDto getInquiry(UUID id);

    void deleteInquiry(UUID id);

    InquiresMetaData getInquires(int pageNum, String sortDir);
}
