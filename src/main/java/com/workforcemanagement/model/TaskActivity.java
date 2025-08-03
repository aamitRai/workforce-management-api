package com.workforcemanagement.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskActivity {
    private String id;
    private String taskId;
    private String userId;
    private String userName;
    private String action;
    private String description;
    private LocalDateTime timestamp;

    public enum ActivityType {
        CREATED, ASSIGNED, REASSIGNED, STATUS_CHANGED, PRIORITY_CHANGED, UPDATED, COMMENT_ADDED
    }
}