package com.unihub.classroom.material.repository;

import com.unihub.classroom.material.model.Material;
import com.unihub.classroom.material.model.enums.MaterialType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MaterialRepository extends JpaRepository<Material, UUID> {

    Optional<Material> findByMidAndCreatedBy(UUID mid,String email);

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

    @Query("""
    SELECT m
    FROM Material m
    JOIN m.classroom c
   WHERE m.classroom.id = :class_id
   AND (
       c.createdBy = :email
       OR EXISTS (
           SELECT 1 FROM Member mem
           WHERE mem.classroom.id = c.id
           AND mem.email = :email
       )
       )
""")
    Page<Material> findMaterials(
            @Param("email") String email,@Param("class_id") UUID classId, Pageable pageable
    );


    @Query("""
    SELECT m
    FROM Material m
    JOIN FETCH m.assignment a
    JOIN m.classroom c
    WHERE m.classroom.id = :class_id
    AND m.materialType = :materialType
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
    List<Material> findAssignments(
            @Param("email") String email,
            @Param("class_id") UUID classId,
            @Param("materialType") MaterialType materialType,
            Pageable pageable
    );
}
