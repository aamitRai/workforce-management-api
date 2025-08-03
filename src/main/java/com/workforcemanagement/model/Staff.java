package com.workforcemanagement.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Staff {
    private String id;
    private String name;
    private String email;
    private String department;
    private String role;
}