package com.franguelfo.familyhub.application.task

import com.franguelfo.familyhub.domain.exception.MemberNotFoundException
import com.franguelfo.familyhub.domain.exception.TaskNotFoundException
import com.franguelfo.familyhub.domain.member.MemberRepository
import com.franguelfo.familyhub.domain.task.Task
import com.franguelfo.familyhub.domain.task.TaskPriority
import com.franguelfo.familyhub.domain.task.TaskRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.UUID

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val memberRepository: MemberRepository
) {

    fun createTask(
        title: String,
        description: String? = null,
        priority: TaskPriority = TaskPriority.MEDIUM,
        assignedTo: UUID? = null,
        dueDate: LocalDate? = null
    ): Task {
        // Validamos la existencia del miembro asignado solo si se envió el campo
        assignedTo?.let { memberId ->
            if (memberRepository.findById(memberId) == null) {
                throw MemberNotFoundException(memberId)
            }
        }

        val task = Task(
            title = title,
            description = description,
            priority = priority,
            assignedTo = assignedTo,
            dueDate = dueDate
        )
        return taskRepository.save(task)
    }

    fun getTaskById(id: UUID): Task =
        taskRepository.findById(id) ?: throw TaskNotFoundException(id)

    fun completeTask(id: UUID): Task {
        val task = getTaskById(id)
        val completedTask = task.complete()
        return taskRepository.save(completedTask)
    }

    fun getAllTasks(): List<Task> = taskRepository.findAll()
}