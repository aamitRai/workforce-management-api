package com.workforcemanagement.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class AssignTaskRequest {
    @NotBlank(message = "New staff ID is required")
    private String newStaffId;

    @NotBlank(message = "Assigned by is required")
    private String assignedBy;
}
