package com.workforcemanagement.mapper;


import com.workforcemanagement.dto.CreateTaskRequest;
import com.workforcemanagement.dto.TaskDto;
import com.workforcemanagement.model.Staff;
import com.workforcemanagement.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;
import java.util.UUID;
@Mapper(componentModel = "spring", imports = {UUID.class, LocalDateTime.class})
public interface TaskMapper {

    @Mapping(target = "id", expression = "java(UUID.randomUUID().toString())")
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "activities", ignore = true)
    @Mapping(target = "comments", ignore = true)
    Task toTask(CreateTaskRequest request);

    @Mapping(target = "id", source = "task.id")  // ✅ Fixed here
    @Mapping(target = "assignedStaffName", source = "assignedStaff.name")
    TaskDto toTaskDto(Task task, Staff assignedStaff);

    @Mapping(target = "assignedStaffName", ignore = true)
    TaskDto toTaskDto(Task task);
}
