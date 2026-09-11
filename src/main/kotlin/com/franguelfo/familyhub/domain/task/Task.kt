package com.franguelfo.familyhub.domain.task

import java.time.LocalDate
import java.util.UUID

data class Task(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val description: String? = null,
    val status: TaskStatus = TaskStatus.PENDING,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val assignedTo: UUID? = null,
    val dueDate: LocalDate? = null
) {
    // Método de dominio que aprovecha copy() y asegura inmutabilidad
    fun complete(): Task {
        return this.copy(status = TaskStatus.COMPLETED)
    }

    fun assignTo(memberId: UUID): Task {
        return this.copy(assignedTo = memberId)
    }
}