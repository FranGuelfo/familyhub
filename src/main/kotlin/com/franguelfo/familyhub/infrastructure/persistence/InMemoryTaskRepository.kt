package com.franguelfo.familyhub.infrastructure.persistence

import com.franguelfo.familyhub.domain.task.Task
import com.franguelfo.familyhub.domain.task.TaskRepository
import com.franguelfo.familyhub.domain.task.TaskStatus
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class InMemoryTaskRepository : TaskRepository {

    private val storage = ConcurrentHashMap<UUID, Task>()

    override fun save(task: Task): Task {
        storage[task.id] = task
        return task
    }

    override fun findById(id: UUID): Task? = storage[id]

    override fun findAll(): List<Task> = storage.values.toList()

    override fun findByAssignedTo(memberId: UUID): List<Task> =
        storage.values.filter { it.assignedTo == memberId }

    override fun findByStatus(status: TaskStatus): List<Task> =
        storage.values.filter { it.status == status }

    override fun deleteById(id: UUID) {
        storage.remove(id)
    }
}