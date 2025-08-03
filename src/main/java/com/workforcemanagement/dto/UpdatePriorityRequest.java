package com.workforcemanagement.dto;

import com.workforcemanagement.model.Task;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class UpdatePriorityRequest {
    @NotNull(message = "Priority is required")
    private Task.TaskPriority priority;

    private String updatedBy;
}
