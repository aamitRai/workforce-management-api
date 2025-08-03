package com.workforcemanagement.service;

import com.workforcemanagement.dto.AddCommentRequest;
import com.workforcemanagement.dto.AssignTaskRequest;
import com.workforcemanagement.dto.CreateTaskRequest;
import com.workforcemanagement.dto.TaskDto;
import com.workforcemanagement.dto.UpdatePriorityRequest;
import com.workforcemanagement.mapper.TaskMapper;
import com.workforcemanagement.model.Staff;
import com.workforcemanagement.model.Task;
import com.workforcemanagement.model.TaskActivity;
import com.workforcemanagement.model.TaskComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskMapper taskMapper;
    private final Map<String, Task> tasks = new HashMap<>();
    private final Map<String, Staff> staff = new HashMap<>();

    {
        staff.put("staff1", Staff.builder()
                .id("staff1")
                .name("John Doe")
                .email("john@company.com")
                .department("Sales")
                .role("Sales Representative")
                .build());

        staff.put("staff2", Staff.builder()
                .id("staff2")
                .name("Jane Smith")
                .email("jane@company.com")
                .department("Operations")
                .role("Operations Manager")
                .build());

        staff.put("staff3", Staff.builder()
                .id("staff3")
                .name("Mike Johnson")
                .email("mike@company.com")
                .department("Sales")
                .role("Senior Sales Representative")
                .build());
    }

    public TaskDto createTask(CreateTaskRequest request) {
        Task task = taskMapper.toTask(request);


        addActivity(task, request.getCreatedBy(), "Task created",
                "Task '" + task.getTitle() + "' was created and assigned to " + getStaffName(request.getAssignedStaffId()));

        tasks.put(task.getId(), task);

        Staff assignedStaff = staff.get(request.getAssignedStaffId());
        return taskMapper.toTaskDto(task, assignedStaff);
    }

    public Optional<TaskDto> getTaskById(String taskId) {
        Task task = tasks.get(taskId);
        if (task == null) {
            return Optional.empty();
        }

        Staff assignedStaff = staff.get(task.getAssignedStaffId());
        return Optional.of(taskMapper.toTaskDto(task, assignedStaff));
    }

    public List<TaskDto> getAllTasks() {
        return tasks.values().stream()
                .filter(task -> task.getStatus() != Task.TaskStatus.CANCELLED)
                .map(task -> {
                    Staff assignedStaff = staff.get(task.getAssignedStaffId());
                    return taskMapper.toTaskDto(task, assignedStaff);
                })
                .collect(Collectors.toList());
    }

    public List<TaskDto> getTasksByStaffId(String staffId) {
        return tasks.values().stream()
                .filter(task -> staffId.equals(task.getAssignedStaffId()))
                .filter(task -> task.getStatus() != Task.TaskStatus.CANCELLED)
                .map(task -> {
                    Staff assignedStaff = staff.get(task.getAssignedStaffId());
                    return taskMapper.toTaskDto(task, assignedStaff);
                })
                .collect(Collectors.toList());
    }

    public List<TaskDto> getTasksByDateRange(LocalDate startDate, LocalDate endDate) {
        return tasks.values().stream()
                .filter(task -> task.getStatus() != Task.TaskStatus.CANCELLED)
                .filter(task -> {
                    LocalDate taskStartDate = task.getStartDate().toLocalDate();
                    boolean startedInRange = !taskStartDate.isBefore(startDate) && !taskStartDate.isAfter(endDate);
                    boolean startedBeforeButOpen = taskStartDate.isBefore(startDate) &&
                            task.getStatus() == Task.TaskStatus.ACTIVE;
                    return startedInRange || startedBeforeButOpen;
                })
                .map(task -> {
                    Staff assignedStaff = staff.get(task.getAssignedStaffId());
                    return taskMapper.toTaskDto(task, assignedStaff);
                })
                .collect(Collectors.toList());
    }

    public List<TaskDto> getTasksByPriority(Task.TaskPriority priority) {
        return tasks.values().stream()
                .filter(task -> task.getStatus() != Task.TaskStatus.CANCELLED)
                .filter(task -> priority.equals(task.getPriority()))
                .map(task -> {
                    Staff assignedStaff = staff.get(task.getAssignedStaffId());
                    return taskMapper.toTaskDto(task, assignedStaff);
                })
                .collect(Collectors.toList());
    }

    public Optional<TaskDto> assignTaskByRef(String taskId, AssignTaskRequest request) {
        Task task = tasks.get(taskId);
        if (task == null) {
            return Optional.empty();
        }

        String oldStaffId = task.getAssignedStaffId();
        String oldStaffName = getStaffName(oldStaffId);
        String newStaffName = getStaffName(request.getNewStaffId());

        if (!oldStaffId.equals(request.getNewStaffId())) {
            task.setStatus(Task.TaskStatus.CANCELLED);
            task.setUpdatedAt(LocalDateTime.now());

            addActivity(task, request.getAssignedBy(), "Task cancelled",
                    "Task cancelled due to reassignment from " + oldStaffName + " to " + newStaffName);

            Task newTask = Task.builder()
                    .id(UUID.randomUUID().toString())
                    .title(task.getTitle())
                    .description(task.getDescription())
                    .status(Task.TaskStatus.ACTIVE)
                    .priority(task.getPriority())
                    .startDate(task.getStartDate())
                    .dueDate(task.getDueDate())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .assignedStaffId(request.getNewStaffId())
                    .createdBy(request.getAssignedBy())
                    .activities(new ArrayList<>())
                    .comments(new ArrayList<>())
                    .build();

            addActivity(newTask, request.getAssignedBy(), "Task reassigned",
                    "Task reassigned from " + oldStaffName + " to " + newStaffName);

            tasks.put(newTask.getId(), newTask);

            Staff assignedStaff = staff.get(request.getNewStaffId());
            return Optional.of(taskMapper.toTaskDto(newTask, assignedStaff));
        }

        Staff assignedStaff = staff.get(task.getAssignedStaffId());
        return Optional.of(taskMapper.toTaskDto(task, assignedStaff));
    }

    public Optional<TaskDto> updateTaskPriority(String taskId, UpdatePriorityRequest request) {
        Task task = tasks.get(taskId);
        if (task == null) {
            return Optional.empty();
        }

        Task.TaskPriority oldPriority = task.getPriority();
        task.setPriority(request.getPriority());
        task.setUpdatedAt(LocalDateTime.now());

        addActivity(task, request.getUpdatedBy(), "Priority changed",
                "Priority changed from " + oldPriority + " to " + request.getPriority());

        Staff assignedStaff = staff.get(task.getAssignedStaffId());
        return Optional.of(taskMapper.toTaskDto(task, assignedStaff));
    }

    public Optional<TaskDto> addComment(String taskId, AddCommentRequest request) {
        Task task = tasks.get(taskId);
        if (task == null) {
            return Optional.empty();
        }

        TaskComment comment = TaskComment.builder()
                .id(UUID.randomUUID().toString())
                .taskId(taskId)
                .userId(request.getUserId())
                .userName(request.getUserName())
                .comment(request.getComment())
                .timestamp(LocalDateTime.now())
                .build();

        task.getComments().add(comment);
        task.setUpdatedAt(LocalDateTime.now());

        addActivity(task, request.getUserId(), "Comment added",
                request.getUserName() + " added a comment");

        Staff assignedStaff = staff.get(task.getAssignedStaffId());
        return Optional.of(taskMapper.toTaskDto(task, assignedStaff));
    }

    public Optional<TaskDto> updateTaskStatus(String taskId, Task.TaskStatus status, String updatedBy) {
        Task task = tasks.get(taskId);
        if (task == null) {
            return Optional.empty();
        }

        Task.TaskStatus oldStatus = task.getStatus();
        task.setStatus(status);
        task.setUpdatedAt(LocalDateTime.now());

        addActivity(task, updatedBy, "Status changed",
                "Status changed from " + oldStatus + " to " + status);

        Staff assignedStaff = staff.get(task.getAssignedStaffId());
        return Optional.of(taskMapper.toTaskDto(task, assignedStaff));
    }

    private void addActivity(Task task, String userId, String action, String description) {
        TaskActivity activity = TaskActivity.builder()
                .id(UUID.randomUUID().toString())
                .taskId(task.getId())
                .userId(userId)
                .userName(getStaffName(userId))
                .action(action)
                .description(description)
                .timestamp(LocalDateTime.now())
                .build();

        task.getActivities().add(activity);
    }

    private String getStaffName(String staffId) {
        Staff staffMember = staff.get(staffId);
        return staffMember != null ? staffMember.getName() : "Unknown User";
    }

    public List<Staff> getAllStaff() {
        return new ArrayList<>(staff.values());
    }
}