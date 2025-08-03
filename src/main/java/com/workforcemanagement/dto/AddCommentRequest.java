package com.workforcemanagement.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class AddCommentRequest {
    @NotBlank(message = "Comment is required")
    private String comment;

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "User name is required")
    private String userName;
}