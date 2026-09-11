package com.franguelfo.familyhub.infrastructure.persistence.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "events")
class CalendarEventJpaEntity(
    @Id
    val id: UUID,

    @Column(name = "family_id", nullable = false)
    val familyId: UUID,

    @Column(nullable = false, length = 150)
    var title: String,

    @Column(columnDefinition = "TEXT")
    var description: String?,

    @Column(name = "start_time", nullable = false)
    var startTime: LocalDateTime,

    @Column(name = "end_time", nullable = false)
    var endTime: LocalDateTime,

    @Column(nullable = false, length = 30)
    var category: String,

    @Column(length = 200)
    var location: String?,

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "event_attendees",
        joinColumns = [JoinColumn(name = "event_id")]
    )
    @Column(name = "member_id")
    var attendeeIds: MutableSet<UUID> = mutableSetOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CalendarEventJpaEntity) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}