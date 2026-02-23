package com.unihub.universitymanagement.universitymanagement.college.repository;

import com.unihub.universitymanagement.universitymanagement.college.model.College;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface CollegeRepository extends JpaRepository<College,UUID> {

    List<College> findAllByUniversity_UniId(Pageable pageable, UUID universityId);

    Optional<College> findByIdAndUniversity_UniId(UUID id, UUID universityId);
}
