## Workforce Management API

A Spring Boot API to manage tasks, staff assignments, activity logs, and comments.

### Getting Started

- Java 17+
- Run with: `./gradlew bootRun`

### API Endpoints
API Usage Steps
Step 1: Get Available Staff
GET http://localhost:8080/api/tasks/staff

Step 2: Create a Task
POST http://localhost:8080/api/tasks
Request Body:
{
  "title": "Test Task 1",
  "description": "Testing task creation",
  "startDate": "2024-01-15T09:00:00",
  "dueDate": "2024-01-20T17:00:00",
  "assignedStaffId": "staff1",
  "createdBy": "manager1",
  "priority": "MEDIUM"
}
Step 3: Get All Tasks
GET http://localhost:8080/api/tasks

Step 4: Get Task Details
GET http://localhost:8080/api/tasks/{taskId}


Step 5: Add Comment to Task
POST http://localhost:8080/api/tasks/{taskId}/comments
Request Body:
{
  "comment": "This is a test comment",
  "userId": "staff1",
  "userName": "John Doe"
}
Step 6: Update Task Priority
PUT http://localhost:8080/api/tasks/{taskId}/priority
Request Body:
{
  "priority": "HIGH",
  "updatedBy": "manager1"
}
Step 7: Reassign Task to Another Staff
PUT http://localhost:8080/api/tasks/{taskId}/assign-by-ref
Request Body:
{
  "newStaffId": "staff2",
  "assignedBy": "manager1"
}
Step 8: Get Tasks by Priority
GET http://localhost:8080/api/tasks/priority/HIGH

Step 9: Get Tasks by Date Range
GET http://localhost:8080/api/tasks/date-range?startDate=2024-01-01&endDate=2024-01-31

Step 10: Get Tasks by Staff ID
GET http://localhost:8080/api/tasks/staff/staff2

Step 11: Update Task Status
PUT http://localhost:8080/api/tasks/{taskId}/status?status=COMPLETED&updatedBy=staff2