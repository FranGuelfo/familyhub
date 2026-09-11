package com.franguelfo.familyhub.infrastructure.web.controller

import com.franguelfo.familyhub.application.task.TaskService
import com.franguelfo.familyhub.infrastructure.web.dto.CreateTaskRequest
import com.franguelfo.familyhub.infrastructure.web.dto.TaskResponse
import com.franguelfo.familyhub.infrastructure.web.dto.toResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tareas", description = "Endpoints de gestión de tareas")
class TaskController(
    private val taskService: TaskService
) {

    @PostMapping("/tasks")
    @Operation(summary = "Crear tarea", description = "Crea una nueva tarea para el usuario autenticado")
    fun createTask(
        @RequestBody request: CreateTaskRequest,
        @AuthenticationPrincipal email: String
    ): ResponseEntity<TaskResponse> {
        // email contiene el subject extraído del token por JwtAuthenticationFilter
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener tarea por ID", description = "Devuelve el detalle de una tarea a partir de su identificador")
    fun getTaskById(@PathVariable id: UUID): ResponseEntity<TaskResponse> =
        ResponseEntity.ok(taskService.getTaskById(id).toResponse())

    @PatchMapping("/{id}/complete")
    @Operation(summary = "Completar tarea", description = "Marca una tarea como completada")
    fun completeTask(@PathVariable id: UUID): ResponseEntity<TaskResponse> =
        ResponseEntity.ok(taskService.completeTask(id).toResponse())

    @GetMapping
    @Operation(summary = "Listar tareas", description = "Devuelve todas las tareas registradas")
    fun getAllTasks(): List<TaskResponse> =
        taskService.getAllTasks().map { it.toResponse() }
}
