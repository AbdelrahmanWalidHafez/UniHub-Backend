package com.unihub.universitymanagement.universitymanagement.college.repository;

import com.unihub.universitymanagement.universitymanagement.college.model.College;
import com.unihub.universitymanagement.universitymanagement.university.model.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface CollegeRepository extends JpaRepository<College,UUID> {

}
