package com.franguelfo.familyhub.domain.family

import com.franguelfo.familyhub.domain.member.Member
import com.franguelfo.familyhub.domain.member.Role
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class FamilyTest {

    @Test
    fun `debe agregar un miembro a la familia manteniendo inmutabilidad`() {
        val family = Family(name = "Familia Guelfo")
        val member = Member(name = "Fran", email = "fran@familyhub.com", role = Role.ADMIN)

        val updatedFamily = family.addMember(member)

        // Verificamos que la original no mutó
        assertTrue(family.members.isEmpty())
        // Verificamos que la nueva tiene el miembro
        assertEquals(1, updatedFamily.members.size)
        assertEquals("Fran", updatedFamily.members.first().name)
    }

    @Test
    fun `debe lanzar excepcion si se intenta agregar un miembro con email duplicado`() {
        val email = "duplicado@familyhub.com"
        val member1 = Member(name = "Fran", email = email)
        val member2 = Member(name = "Francisco", email = email)

        val familyWithOneMember = Family(name = "Familia").addMember(member1)

        val exception = assertThrows<IllegalArgumentException> {
            familyWithOneMember.addMember(member2)
        }

        assertTrue(exception.message!!.contains("ya pertenece a la familia"))
    }
}