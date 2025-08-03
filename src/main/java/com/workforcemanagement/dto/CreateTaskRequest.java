package com.workforcemanagement.dto;

import com.workforcemanagement.model.Task;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class CreateTaskRequest {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    @NotNull(message = "Due date is required")
    private LocalDateTime dueDate;

    @NotBlank(message = "Assigned staff ID is required")
    private String assignedStaffId;

    @NotBlank(message = "Created by is required")
    private String createdBy;

    private Task.TaskPriority priority = Task.TaskPriority.MEDIUM;
}