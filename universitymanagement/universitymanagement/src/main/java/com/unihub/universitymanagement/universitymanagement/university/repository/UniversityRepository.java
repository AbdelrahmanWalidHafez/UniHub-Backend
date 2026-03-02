package com.unihub.universitymanagement.universitymanagement.university.repository;

import com.unihub.universitymanagement.universitymanagement.university.model.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UniversityRepository extends JpaRepository<University, UUID> {

    @Query("""
    SELECT u
    FROM University u
    WHERE LOWER(u.universityName) LIKE LOWER(CONCAT('%', :searchText, '%'))
""")
    List<University> searchUniversityNames(@Param("searchText") String searchText);

    long countBySubscriptionPlan_Pid(UUID pid);

}
