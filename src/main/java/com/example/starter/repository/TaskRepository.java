package com.example.starter.repository;

import com.example.starter.model.Priority;
import com.example.starter.model.TaskItem;
import com.example.starter.model.TaskStatus;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    TaskItem save(TaskItem task);

    Optional<TaskItem> findById(String id);

    List<TaskItem> findAll();

    List<TaskItem> findByFilters(TaskStatus status, Priority priority, String searchKeyword);

    boolean existsById(String id);

    boolean deleteById(String id);

    long count();
}
