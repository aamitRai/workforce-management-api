package com.workforcemanagement.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private String id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String assignedStaffId;
    private String createdBy;

    @Builder.Default
    private List<TaskActivity> activities = new ArrayList<>();

    @Builder.Default
    private List<TaskComment> comments = new ArrayList<>();

    public enum TaskStatus {
        ACTIVE, COMPLETED, CANCELLED
    }

    public enum TaskPriority {
        HIGH, MEDIUM, LOW
    }
}