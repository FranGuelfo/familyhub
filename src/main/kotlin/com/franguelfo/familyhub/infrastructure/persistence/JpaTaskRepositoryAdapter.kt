package com.franguelfo.familyhub.infrastructure.persistence

import com.franguelfo.familyhub.domain.task.Task
import com.franguelfo.familyhub.domain.task.TaskRepository
import com.franguelfo.familyhub.domain.task.TaskStatus
import com.franguelfo.familyhub.infrastructure.persistence.mapper.toDomain
import com.franguelfo.familyhub.infrastructure.persistence.mapper.toEntity
import com.franguelfo.familyhub.infrastructure.persistence.repository.SpringDataTaskRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class JpaTaskRepositoryAdapter(
    private val springDataTaskRepository: SpringDataTaskRepository
) : TaskRepository {

    override fun save(task: Task): Task =
        springDataTaskRepository.save(task.toEntity()).toDomain()

    override fun findById(id: UUID): Task? =
        springDataTaskRepository.findByIdOrNull(id)?.toDomain()

    override fun findByAssignedTo(memberId: UUID): List<Task> =
        springDataTaskRepository.findByAssignedTo(memberId).map { it.toDomain() }

    override fun findByStatus(status: TaskStatus): List<Task> =
        springDataTaskRepository.findByStatus(status.name).map { it.toDomain() }

    override fun findAll(): List<Task> =
        springDataTaskRepository.findAll().map { it.toDomain() }

    override fun deleteById(id: UUID) {
        springDataTaskRepository.deleteById(id)
    }
}