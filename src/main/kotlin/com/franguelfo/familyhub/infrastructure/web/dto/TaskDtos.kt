package com.franguelfo.familyhub.infrastructure.web.dto

import com.franguelfo.familyhub.domain.task.Task
import com.franguelfo.familyhub.domain.task.TaskPriority
import com.franguelfo.familyhub.domain.task.TaskStatus
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate
import java.util.UUID

data class CreateTaskRequest(
    @field:NotBlank(message = "El título de la tarea es obligatorio")
    val title: String,

    val description: String? = null,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val assignedTo: UUID? = null,
    val dueDate: LocalDate? = null
)

data class TaskResponse(
    val id: UUID,
    val title: String,
    val description: String?,
    val status: TaskStatus,
    val priority: TaskPriority,
    val assignedTo: UUID?,
    val dueDate: LocalDate?
)

fun Task.toResponse(): TaskResponse = TaskResponse(
    id = this.id,
    title = this.title,
    description = this.description,
    status = this.status,
    priority = this.priority,
    assignedTo = this.assignedTo,
    dueDate = this.dueDate
)