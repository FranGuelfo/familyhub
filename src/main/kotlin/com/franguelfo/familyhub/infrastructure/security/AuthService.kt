package com.franguelfo.familyhub.infrastructure.security

import com.franguelfo.familyhub.domain.member.Member
import com.franguelfo.familyhub.domain.member.MemberRepository
import com.franguelfo.familyhub.domain.member.Role
import com.franguelfo.familyhub.infrastructure.security.jwt.JwtService
import com.franguelfo.familyhub.infrastructure.web.dto.AuthResponse
import com.franguelfo.familyhub.infrastructure.web.dto.LoginRequest
import com.franguelfo.familyhub.infrastructure.web.dto.RegisterRequest
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class AuthService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) {

    @Transactional
    fun register(request: RegisterRequest): AuthResponse {
        val existing = memberRepository.findByEmail(request.email)
        if (existing != null) {
            throw IllegalArgumentException("El email ya está registrado")
        }

        // 1. Aseguramos entrada no nula
        val rawPassword: String = request.password

        // 2. Aseguramos que el hash resultante tampoco sea tratado como String?
        val encodedPassword: String = passwordEncoder.encode(rawPassword) ?: ""

        val member = Member(
            id = UUID.randomUUID(),
            name = request.name,
            email = request.email,
            role = Role.valueOf(request.role.uppercase()),
            password = encodedPassword
        )

        val saved = memberRepository.save(member)
        val token = jwtService.generateToken(saved.id, saved.email, saved.role.name)

        return AuthResponse(
            token = token,
            memberId = saved.id,
            email = saved.email,
            role = saved.role.name
        )
    }

    fun login(request: LoginRequest): AuthResponse {
        val rawPassword: String = request.password

        val member = memberRepository.findByEmail(request.email)
            ?: throw BadCredentialsException("Credenciales inválidas")

        if (!passwordEncoder.matches(rawPassword, member.password)) {
            throw BadCredentialsException("Credenciales inválidas")
        }

        val token = jwtService.generateToken(member.id, member.email, member.role.name)

        return AuthResponse(
            token = token,
            memberId = member.id,
            email = member.email,
            role = member.role.name
        )
    }
}