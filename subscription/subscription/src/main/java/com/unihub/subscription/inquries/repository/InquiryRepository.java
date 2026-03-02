package com.unihub.subscription.inquries.repository;

import com.unihub.subscription.inquries.model.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InquiryRepository extends JpaRepository<Inquiry, UUID> {
}
