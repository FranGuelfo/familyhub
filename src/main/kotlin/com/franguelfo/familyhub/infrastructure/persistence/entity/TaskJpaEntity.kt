package com.franguelfo.familyhub.infrastructure.persistence.entity

import jakarta.persistence.*
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "tasks")
class TaskJpaEntity(
    @Id
    val id: UUID,

    @Column(nullable = false, length = 200)
    var title: String,

    @Column(columnDefinition = "TEXT")
    var description: String?,

    @Column(nullable = false, length = 20)
    var status: String,

    @Column(nullable = false, length = 20)
    var priority: String,

    @Column(name = "assigned_to")
    var assignedTo: UUID?,

    @Column(name = "due_date")
    var dueDate: LocalDate?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TaskJpaEntity) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}