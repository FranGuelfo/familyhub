package com.franguelfo.familyhub.application.task

import com.franguelfo.familyhub.domain.exception.MemberNotFoundException
import com.franguelfo.familyhub.domain.exception.TaskNotFoundException
import com.franguelfo.familyhub.domain.member.Member
import com.franguelfo.familyhub.domain.member.MemberRepository
import com.franguelfo.familyhub.domain.task.Task
import com.franguelfo.familyhub.domain.task.TaskPriority
import com.franguelfo.familyhub.domain.task.TaskRepository
import com.franguelfo.familyhub.domain.task.TaskStatus
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class TaskServiceTest {

    // Instanciación directa de mocks con la función genérica mockk()
    private val taskRepository: TaskRepository = mockk()
    private val memberRepository: MemberRepository = mockk()

    // Clase bajo prueba instanciada por constructor sin levantar Spring
    private val taskService = TaskService(taskRepository, memberRepository)

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `debe crear una tarea correctamente cuando no tiene miembro asignado`() {
        // Given
        val title = "Comprar leche"
        val taskToSave = Task(title = title)

        every { taskRepository.save(any()) } answers { firstArg() }

        // When
        val result = taskService.createTask(title = title)

        // Then
        assertNotNull(result.id)
        assertEquals(title, result.title)
        assertEquals(TaskStatus.PENDING, result.status)
        assertNull(result.assignedTo)

        // Verificamos que no se consultó el repositorio de miembros
        verify(exactly = 0) { memberRepository.findById(any()) }
        verify(exactly = 1) { taskRepository.save(any()) }
    }

    @Test
    fun `debe lanzar MemberNotFoundException al crear tarea si el miembro asignado no existe`() {
        // Given
        val nonExistentMemberId = UUID.randomUUID()
        every { memberRepository.findById(nonExistentMemberId) } returns null
        
        // When & Then (assertThrows idiomático de JUnit 5)
        assertThrows<MemberNotFoundException> {
            taskService.createTask(
                title = "Arreglar persiana",
                assignedTo = nonExistentMemberId
            )
        }

        // Si el miembro no existe, la tarea NUNCA debe guardarse
        verify(exactly = 0) { taskRepository.save(any()) }
    }

    @Test
    fun `debe completar una tarea existente cambiando su estado a COMPLETED`() {
        // Given
        val taskId = UUID.randomUUID()
        val existingTask = Task(id = taskId, title = "Lavar coche", status = TaskStatus.PENDING)

        every { taskRepository.findById(taskId) } returns existingTask
        every { taskRepository.save(any()) } answers { firstArg() }

        // When
        val result = taskService.completeTask(taskId)

        // Then
        assertEquals(TaskStatus.COMPLETED, result.status)
        assertEquals(taskId, result.id)
        verify(exactly = 1) { taskRepository.findById(taskId) }
        verify(exactly = 1) { taskRepository.save(match { it.status == TaskStatus.COMPLETED }) }
    }

    @Test
    fun `debe lanzar TaskNotFoundException al intentar completar una tarea inexistente`() {
        // Given
        val nonExistentTaskId = UUID.randomUUID()
        every { taskRepository.findById(nonExistentTaskId) } returns null

        // When & Then
        assertThrows<TaskNotFoundException> {
            taskService.completeTask(nonExistentTaskId)
        }

        verify(exactly = 0) { taskRepository.save(any()) }
    }
}