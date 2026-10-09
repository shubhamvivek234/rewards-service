package com.example.starter.repository;

import com.example.starter.model.Priority;
import com.example.starter.model.TaskItem;
import com.example.starter.model.TaskStatus;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryTaskRepository implements TaskRepository {

    private final Map<String, TaskItem> storage = new ConcurrentHashMap<>();

    public InMemoryTaskRepository() {
        // Seed default sample tasks for immediate out-of-the-box exploration
        seedSampleTasks();
    }

    private void seedSampleTasks() {
        Instant now = Instant.now();
        save(new TaskItem(
                "task-101",
                "Explore Spring Boot project structure",
                "Familiarize with the controller, service, and repository architecture.",
                TaskStatus.COMPLETED,
                Priority.HIGH,
                now.minusSeconds(7200),
                now.minusSeconds(3600)
        ));
        save(new TaskItem(
                "task-102",
                "Test REST endpoints in IDE or Postman",
                "Execute GET, POST, PUT, and DELETE operations via curl or an API client.",
                TaskStatus.IN_PROGRESS,
                Priority.URGENT,
                now.minusSeconds(3600),
                now.minusSeconds(1800)
        ));
        save(new TaskItem(
                "task-103",
                "Add database persistence layer",
                "Optionally connect Spring Data JPA or MongoDB for persistent storage.",
                TaskStatus.PENDING,
                Priority.MEDIUM,
                now.minusSeconds(900),
                now.minusSeconds(900)
        ));
    }

    @Override
    public TaskItem save(TaskItem task) {
        storage.put(task.id(), task);
        return task;
    }

    @Override
    public Optional<TaskItem> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<TaskItem> findAll() {
        return storage.values().stream()
                .sorted(Comparator.comparing(TaskItem::createdAt).reversed())
                .toList();
    }

    @Override
    public List<TaskItem> findByFilters(TaskStatus status, Priority priority, String searchKeyword) {
        return storage.values().stream()
                .filter(task -> status == null || task.status() == status)
                .filter(task -> priority == null || task.priority() == priority)
                .filter(task -> {
                    if (searchKeyword == null || searchKeyword.isBlank()) {
                        return true;
                    }
                    String lowerKeyword = searchKeyword.toLowerCase();
                    boolean matchTitle = task.title() != null && task.title().toLowerCase().contains(lowerKeyword);
                    boolean matchDesc = task.description() != null && task.description().toLowerCase().contains(lowerKeyword);
                    return matchTitle || matchDesc;
                })
                .sorted(Comparator.comparing(TaskItem::createdAt).reversed())
                .toList();
    }

    @Override
    public boolean existsById(String id) {
        return storage.containsKey(id);
    }

    @Override
    public boolean deleteById(String id) {
        return storage.remove(id) != null;
    }

    @Override
    public long count() {
        return storage.size();
    }
}
