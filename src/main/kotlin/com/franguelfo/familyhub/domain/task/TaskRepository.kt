package com.franguelfo.familyhub.domain.task

import java.util.UUID

interface TaskRepository {
    fun save(task: Task): Task
    fun findById(id: UUID): Task?
    fun findAll(): List<Task>
    fun findByAssignedTo(memberId: UUID): List<Task>
    fun findByStatus(status: TaskStatus): List<Task>
    fun deleteById(id: UUID)
}