package com.franguelfo.familyhub.infrastructure.persistence.entity

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "families")
class FamilyJpaEntity(
    @Id
    val id: UUID,

    @Column(nullable = false, length = 100)
    var name: String,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "family_members",
        joinColumns = [JoinColumn(name = "family_id")],
        inverseJoinColumns = [JoinColumn(name = "member_id")]
    )
    var members: MutableSet<MemberJpaEntity> = mutableSetOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FamilyJpaEntity) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}