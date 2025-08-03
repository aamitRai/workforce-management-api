package com.workforcemanagement.dto;

import com.workforcemanagement.model.Task;
import com.workforcemanagement.model.TaskActivity;
import com.workforcemanagement.model.TaskComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private String id;
    private String title;
    private String description;
    private Task.TaskStatus status;
    private Task.TaskPriority priority;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String assignedStaffId;
    private String assignedStaffName;
    private String createdBy;
    private List<TaskActivity> activities;
    private List<TaskComment> comments;
}
