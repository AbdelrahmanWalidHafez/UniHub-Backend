package com.unihub.subscription.subscriptionrequest.repository;

import com.unihub.subscription.subscriptionrequest.model.Status;
import com.unihub.subscription.subscriptionrequest.model.SubscriptionRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SubscriptionRequestRepository extends JpaRepository<SubscriptionRequest, UUID> {
    Page<SubscriptionRequest> findAllByStatus(Status status, Pageable pageable);
}
