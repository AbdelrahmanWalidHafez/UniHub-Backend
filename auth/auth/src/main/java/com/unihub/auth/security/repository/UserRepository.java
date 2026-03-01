package com.unihub.auth.security.repository;

import com.unihub.auth.accountmanagement.dto.response.UserMetaDataResponse;
import com.unihub.auth.security.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> , JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByUidAndUniversityMetadata_Tid(UUID id,UUID tid);

    @Query("SELECT COUNT(u) FROM User u " +
            "JOIN u.universityMetadata um " +
            "WHERE um.tid = :tid")
    long countByUniversityTid(@Param("tid") UUID tid);

    @Query("SELECT COUNT(u) FROM User u " +
            "JOIN u.universityMetadata um " +
            "WHERE um.cid = :cid")
    long countByUniversityCid(@Param("cid") UUID cid);


    @Query("""
    SELECT new com.unihub.auth.accountmanagement.dto.response.UserMetaDataResponse(
        u.uid,
        u.email,
        m.cid,
        r.name,
        u.createdBy,
        u.createdAt
    )
    FROM User u
    LEFT JOIN u.role r
    LEFT JOIN u.universityMetadata m
    WHERE m.tid = :tid
    AND (:roleName IS NULL OR r.name = :roleName)
    AND (:cid IS NULL OR m.cid = :cid)
    AND (:email IS NULL OR u.email <> :email)
""")
    Page<UserMetaDataResponse> getUsers(
            @Param("roleName") String roleName,
            @Param("cid") UUID cid,
            @Param("tid") UUID tid,
            @Param("email")String email,
            Pageable pageable
    );

    @Query("""
    SELECT u
    FROM User u
    JOIN u.universityMetadata m
    WHERE m.tid = :tid
    AND u.email <> :currentEmail
      AND (
            LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
         OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
         OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
      )
""")
    List<User> searchUsers(@Param("keyword") String keyword,
                           @Param("tid") UUID tid,
                           @Param("currentEmail") String currentEmail,
                           Pageable pageable);
}
