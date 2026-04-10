package com.unihub.classroom.clazz.repository;

import com.unihub.classroom.clazz.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository <Member, UUID>{

    Optional<Member> findByEmailAndClassroom_Id(String email, UUID classroomId);

    Page<Member> findByClassroom_Id(UUID classroomId, Pageable pageable);
}
