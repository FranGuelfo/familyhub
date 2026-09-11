package com.franguelfo.familyhub.application.member

import com.franguelfo.familyhub.domain.exception.MemberNotFoundException
import com.franguelfo.familyhub.domain.member.Member
import com.franguelfo.familyhub.domain.member.MemberRepository
import com.franguelfo.familyhub.domain.member.Role
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {

    fun createMember(name: String, email: String, role: Role = Role.MEMBER): Member {
        memberRepository.findByEmail(email)?.let {
            throw IllegalArgumentException("Ya existe un miembro registrado con el email '$email'")
        }

        val member = Member(name = name, email = email, role = role)
        return memberRepository.save(member)
    }

    fun getMemberById(id: UUID): Member {
        return memberRepository.findById(id) ?: throw MemberNotFoundException(id)
    }

    fun getAllMembers(): List<Member> = memberRepository.findAll()
}