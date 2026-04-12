package com.unihub.classroom.material.repository;

import com.unihub.classroom.material.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface MaterialRepository extends JpaRepository<Material, UUID> {

    Optional<Material> findByMidAndCreatedBy(UUID mid,String email);

    Optional<Material> findByIdAndClassroom_Id(UUID mid, UUID cid);

    @Query("""
    SELECT m
    FROM Material m
    JOIN m.classroom c
    WHERE m.mid = :materialId
    AND (
        c.createdBy = :email
        OR EXISTS (
            SELECT 1
            FROM Member mem
            WHERE mem.classroom.id = c.id
            AND mem.email = :email
        )
    )
""")
    Optional<Material> findMaterial(
            @Param("materialId") UUID materialId,
            @Param("email") String email
    );
}
