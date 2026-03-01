package com.unihub.auth.security.repository;

import com.unihub.auth.accountmanagement.dto.response.UserMetaDataResponse;
import com.unihub.auth.security.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query(value = """
        SELECT *
        FROM users
        WHERE LOWER(email) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(first_name) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(last_name) LIKE LOWER(CONCAT('%', :keyword, '%'))
        LIMIT 5
    """, nativeQuery = true)
    List<User> searchUsers(@Param("keyword") String keyword);

}
