package com.workforcemanagement.controller;

import com.workforcemanagement.dto.AddCommentRequest;
import com.workforcemanagement.dto.AssignTaskRequest;
import com.workforcemanagement.dto.CreateTaskRequest;
import com.workforcemanagement.dto.TaskDto;
import com.workforcemanagement.dto.UpdatePriorityRequest;
import com.workforcemanagement.model.Staff;
import com.workforcemanagement.model.Task;
import com.workforcemanagement.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Validated
public class TaskController {

    private final TaskService taskService;


    @PostMapping
    public ResponseEntity<TaskDto> createTask(@Valid @RequestBody CreateTaskRequest request) {
        TaskDto task = taskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable String taskId) {
        return taskService.getTaskById(taskId)
                .map(task -> ResponseEntity.ok(task))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        List<TaskDto> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/staff/{staffId}")
    public ResponseEntity<List<TaskDto>> getTasksByStaffId(@PathVariable String staffId) {
        List<TaskDto> tasks = taskService.getTasksByStaffId(staffId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<TaskDto>> getTasksByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<TaskDto> tasks = taskService.getTasksByDateRange(startDate, endDate);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<TaskDto>> getTasksByPriority(@PathVariable Task.TaskPriority priority) {
        List<TaskDto> tasks = taskService.getTasksByPriority(priority);
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{taskId}/assign-by-ref")
    public ResponseEntity<TaskDto> assignTaskByRef(
            @PathVariable String taskId,
            @Valid @RequestBody AssignTaskRequest request) {
        return taskService.assignTaskByRef(taskId, request)
                .map(task -> ResponseEntity.ok(task))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{taskId}/priority")
    public ResponseEntity<TaskDto> updateTaskPriority(
            @PathVariable String taskId,
            @Valid @RequestBody UpdatePriorityRequest request) {
        return taskService.updateTaskPriority(taskId, request)
                .map(task -> ResponseEntity.ok(task))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{taskId}/comments")
    public ResponseEntity<TaskDto> addComment(
            @PathVariable String taskId,
            @Valid @RequestBody AddCommentRequest request) {
        return taskService.addComment(taskId, request)
                .map(task -> ResponseEntity.ok(task))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{taskId}/status")
    public ResponseEntity<TaskDto> updateTaskStatus(
            @PathVariable String taskId,
            @RequestParam Task.TaskStatus status,
            @RequestParam String updatedBy) {
        return taskService.updateTaskStatus(taskId, status, updatedBy)
                .map(task -> ResponseEntity.ok(task))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/staff")
    public ResponseEntity<List<Staff>> getAllStaff() {
        List<Staff> staff = taskService.getAllStaff();
        return ResponseEntity.ok(staff);
    }
}