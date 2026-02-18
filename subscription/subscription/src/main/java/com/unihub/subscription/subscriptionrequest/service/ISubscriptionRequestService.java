package com.unihub.subscription.subscriptionrequest.service;

import com.unihub.subscription.subscriptionrequest.dto.request.DeleteFileRequest;
import com.unihub.subscription.subscriptionrequest.dto.request.SubscriptionRequestDto;
import com.unihub.subscription.subscriptionrequest.dto.request.UpdateSubscriptionRequestDto;
import com.unihub.subscription.subscriptionrequest.dto.request.UploadFileRequest;
import com.unihub.subscription.subscriptionrequest.dto.response.AfterUpdateResponse;
import com.unihub.subscription.subscriptionrequest.dto.response.SubscriptionsMetaData;
import com.unihub.subscription.subscriptionrequest.dto.response.SubscriptionRequestResponseDto;
import com.unihub.subscription.subscriptionrequest.model.Status;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


public interface ISubscriptionRequestService {
    SubscriptionRequestResponseDto createSubscriptionRequest(SubscriptionRequestDto subscriptionRequestDto, MultipartFile accreditation, MultipartFile logo) throws IOException;

    SubscriptionRequestResponseDto getSubscription(UUID id) throws IOException;

    List<SubscriptionsMetaData> getSubscriptionRequests(int pageNum, String sortDir, String sortFiled, Status status);

    void deleteSubscriptionRequest(UUID id) throws IOException;

    void uploadFile(UploadFileRequest uploadFileRequest);

    void deleteFile(DeleteFileRequest deleteFileRequest);

    AfterUpdateResponse updateSubscriptionStatus(UpdateSubscriptionRequestDto updateSubscriptionRequestDto, UUID id);

}
