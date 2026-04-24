package com.unihub.taskmanager.repository;

import com.unihub.taskmanager.model.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository <Task, UUID> {

    Optional<Task> findByIdAndCreatedBy(UUID id,String email);

    List<Task> findAllByIdInAndCreatedBy(List<UUID> ids, String email);

    List<Task> findByCreateBy(String email, Pageable pageable);
}