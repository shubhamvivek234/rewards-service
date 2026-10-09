package com.example.starter.service;

import com.example.starter.model.CreateTaskRequest;
import com.example.starter.model.Priority;
import com.example.starter.model.TaskItem;
import com.example.starter.model.TaskStatus;
import com.example.starter.model.UpdateTaskRequest;

import java.util.List;

public interface TaskService {

    List<TaskItem> getTasks(TaskStatus status, Priority priority, String search);

    TaskItem getTaskById(String id);

    TaskItem createTask(CreateTaskRequest request);

    TaskItem updateTask(String id, UpdateTaskRequest request);

    TaskItem updateTaskStatus(String id, TaskStatus status);

    void deleteTask(String id);
}
