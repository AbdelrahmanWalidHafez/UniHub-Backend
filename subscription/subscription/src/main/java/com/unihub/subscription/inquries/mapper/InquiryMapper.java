package com.unihub.subscription.inquries.mapper;

import com.unihub.subscription.inquries.dto.request.InquiryRequestDto;
import com.unihub.subscription.inquries.dto.response.InquiryMetaData;
import com.unihub.subscription.inquries.dto.response.InquiryResponseDto;
import com.unihub.subscription.inquries.model.Inquiry;
import org.springframework.stereotype.Component;

@Component
public class InquiryMapper {

    public Inquiry toEntity(InquiryRequestDto inquiryRequestDto){
        Inquiry inquiry= new Inquiry();
        inquiry.setCustomerEmail(inquiryRequestDto.getCustomerEmail());
        inquiry.setContent(inquiryRequestDto.getContent());
        inquiry.setSubject(inquiryRequestDto.getSubject());
        return inquiry;
    }

    public InquiryResponseDto toDto(Inquiry inquiry){
        InquiryResponseDto inquiryResponseDto=new InquiryResponseDto();
        inquiryResponseDto.setCustomerEmail(inquiry.getCustomerEmail());
        inquiryResponseDto.setContent(inquiry.getContent());
        inquiryResponseDto.setSubject(inquiry.getSubject());
        return inquiryResponseDto;
    }

    public InquiryMetaData toMetaData(Inquiry inquiry){
        InquiryMetaData inquiryMetaData=new InquiryMetaData();
        inquiryMetaData.setCustomerEmail(inquiry.getCustomerEmail());
        inquiryMetaData.setSubject(inquiry.getSubject());
        inquiryMetaData.setCreatedAt(inquiry.getCreatedAt());
        return inquiryMetaData;
    }
}
