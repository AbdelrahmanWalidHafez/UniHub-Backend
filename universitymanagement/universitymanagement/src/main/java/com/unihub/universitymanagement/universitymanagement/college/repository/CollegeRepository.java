package com.unihub.universitymanagement.universitymanagement.college.repository;

import com.unihub.universitymanagement.universitymanagement.college.model.College;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface CollegeRepository extends JpaRepository<College,UUID> {

    List<College> findAllByUniversity_UniId(Pageable pageable, UUID universityId);

    Optional<College> findByIdAndUniversity_UniId(UUID id, UUID universityId);

    @Query("""
    SELECT c
    FROM College c
    WHERE c.university.uniId = :universityId
      AND (
            LOWER(c.collegeName) LIKE LOWER(CONCAT('%', :keyword, '%'))
         OR LOWER(c.campus) LIKE LOWER(CONCAT('%', :keyword, '%'))
      )
""")
    List<College> searchByUniversity(@Param("universityId") UUID universityId, @Param("keyword") String keyword);
}
