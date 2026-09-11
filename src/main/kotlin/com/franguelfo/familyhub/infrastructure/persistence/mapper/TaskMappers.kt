package com.franguelfo.familyhub.infrastructure.persistence.mapper

import com.franguelfo.familyhub.domain.task.TaskPriority
import com.franguelfo.familyhub.domain.task.Task
import com.franguelfo.familyhub.domain.task.TaskStatus
import com.franguelfo.familyhub.infrastructure.persistence.entity.TaskJpaEntity

fun Task.toEntity(): TaskJpaEntity = TaskJpaEntity(
    id = this.id,
    title = this.title,
    description = this.description,
    status = this.status.name,
    priority = this.priority.name,
    assignedTo = this.assignedTo,
    dueDate = this.dueDate
)

fun TaskJpaEntity.toDomain(): Task = Task(
    id = this.id,
    title = this.title,
    description = this.description,
    status = TaskStatus.valueOf(this.status),
    priority = TaskPriority.valueOf(this.priority),
    assignedTo = this.assignedTo,
    dueDate = this.dueDate
)