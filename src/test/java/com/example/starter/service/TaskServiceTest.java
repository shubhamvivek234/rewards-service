package com.example.starter.service;

import com.example.starter.exception.ResourceNotFoundException;
import com.example.starter.model.CreateTaskRequest;
import com.example.starter.model.Priority;
import com.example.starter.model.TaskItem;
import com.example.starter.model.TaskStatus;
import com.example.starter.model.UpdateTaskRequest;
import com.example.starter.repository.InMemoryTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskServiceTest {

    private InMemoryTaskRepository repository;
    private TaskServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryTaskRepository();
        service = new TaskServiceImpl(repository);
    }

    @Test
    @DisplayName("Should retrieve seed tasks")
    void testGetTasks() {
        List<TaskItem> tasks = service.getTasks(null, null, null);
        assertThat(tasks).isNotEmpty();
        assertThat(tasks.size()).isGreaterThanOrEqualTo(3);
    }

    @Test
    @DisplayName("Should filter tasks by status and priority")
    void testFilterTasks() {
        List<TaskItem> inProgress = service.getTasks(TaskStatus.IN_PROGRESS, null, null);
        assertThat(inProgress).allMatch(t -> t.status() == TaskStatus.IN_PROGRESS);

        List<TaskItem> searchMatches = service.getTasks(null, null, "Spring");
        assertThat(searchMatches).isNotEmpty();
    }

    @Test
    @DisplayName("Should create new task successfully")
    void testCreateTask() {
        CreateTaskRequest request = new CreateTaskRequest(
                "Write comprehensive unit tests",
                "Ensure services and controllers are well tested",
                Priority.HIGH
        );

        TaskItem created = service.createTask(request);

        assertThat(created.id()).isNotBlank();
        assertThat(created.title()).isEqualTo("Write comprehensive unit tests");
        assertThat(created.status()).isEqualTo(TaskStatus.PENDING);
        assertThat(created.priority()).isEqualTo(Priority.HIGH);

        TaskItem fetched = service.getTaskById(created.id());
        assertThat(fetched).isNotNull();
        assertThat(fetched.id()).isEqualTo(created.id());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException for unknown ID")
    void testGetUnknownTaskThrows() {
        assertThatThrownBy(() -> service.getTaskById("non-existent-id"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Task not found with ID");
    }

    @Test
    @DisplayName("Should update existing task")
    void testUpdateTask() {
        UpdateTaskRequest updateReq = new UpdateTaskRequest(
                "Updated Title",
                "Updated description",
                TaskStatus.COMPLETED,
                Priority.LOW
        );

        TaskItem updated = service.updateTask("task-101", updateReq);

        assertThat(updated.title()).isEqualTo("Updated Title");
        assertThat(updated.description()).isEqualTo("Updated description");
        assertThat(updated.status()).isEqualTo(TaskStatus.COMPLETED);
        assertThat(updated.priority()).isEqualTo(Priority.LOW);
    }

    @Test
    @DisplayName("Should update task status directly")
    void testUpdateStatus() {
        TaskItem updated = service.updateTaskStatus("task-103", TaskStatus.IN_PROGRESS);
        assertThat(updated.status()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("Should delete task successfully")
    void testDeleteTask() {
        service.deleteTask("task-101");
        assertThatThrownBy(() -> service.getTaskById("task-101"))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
