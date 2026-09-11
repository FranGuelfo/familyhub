package com.franguelfo.familyhub.domain.family

import com.franguelfo.familyhub.domain.member.Member
import java.util.UUID

data class Family(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val members: List<Member> = emptyList()
) {
    /**
     * Regla de negocio: añade un miembro asegurando que no existan
     * duplicados por ID o email dentro de la misma familia.
     */
    fun addMember(member: Member): Family {
        val alreadyExists = members.any { it.id == member.id || it.email.equals(member.email, ignoreCase = true) }

        if (alreadyExists) {
            throw IllegalArgumentException("El miembro con email '${member.email}' ya pertenece a la familia.")
        }

        // Devolvemos una nueva instancia inmutable con la lista ampliada
        return this.copy(members = members + member)
    }

    /**
     * Regla de negocio: elimina un miembro de la familia por su ID.
     */
    fun removeMember(memberId: UUID): Family {
        return this.copy(members = members.filterNot { it.id == memberId })
    }
}