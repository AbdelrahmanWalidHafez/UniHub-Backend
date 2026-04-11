package com.unihub.classroom.material.repository;

import com.unihub.classroom.material.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MaterialRepository extends JpaRepository<Material, UUID> {

    Optional<Material> findByMidAndCreatedBy(UUID mid,String email);
}
