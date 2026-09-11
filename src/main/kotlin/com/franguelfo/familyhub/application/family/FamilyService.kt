package com.franguelfo.familyhub.application.family

import com.franguelfo.familyhub.domain.exception.FamilyNotFoundException
import com.franguelfo.familyhub.domain.exception.MemberNotFoundException
import com.franguelfo.familyhub.domain.family.Family
import com.franguelfo.familyhub.domain.family.FamilyRepository
import com.franguelfo.familyhub.domain.member.MemberRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class FamilyService(
    private val familyRepository: FamilyRepository,
    private val memberRepository: MemberRepository
) {

    fun createFamily(name: String): Family {
        val family = Family(name = name)
        return familyRepository.save(family)
    }

    fun getFamilyById(id: UUID): Family {
        return familyRepository.findById(id) ?: throw FamilyNotFoundException(id)
    }

    fun addMemberToFamily(familyId: UUID, memberId: UUID): Family {
        val family = getFamilyById(familyId)
        val member = memberRepository.findById(memberId) ?: throw MemberNotFoundException(memberId)

        val updatedFamily = family.addMember(member)
        return familyRepository.save(updatedFamily)
    }

    fun getAllFamilies(): List<Family> = familyRepository.findAll()
}