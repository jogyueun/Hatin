package com.project1.hatin.member.service

import com.project1.hatin.common.authority.JwtTokenProvider
import com.project1.hatin.common.dto.TokenInfo
import com.project1.hatin.common.enums.Role
import com.project1.hatin.common.exception.member.InvaliduserIdException
import com.project1.hatin.member.dto.LoginDto
import com.project1.hatin.member.dto.MemberRequestDto
import com.project1.hatin.member.entity.Member
import com.project1.hatin.member.entity.MemberRole
import com.project1.hatin.member.repository.MemberRepository
import com.project1.hatin.member.repository.MemberRoleRepository
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.transaction.Transactional
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.stereotype.Service
@Tag(name = "회원 Api 컨트롤러", description = "회원 가입, 로그인, Api 명세서 입니다.")

@Transactional
@Service
class MemberService (
    private val memberRepository : MemberRepository,
    private val memberRoleRepository: MemberRoleRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder
){
    fun signUp(memberRequestDto : MemberRequestDto) : String {
        var member: Member? = memberRepository.findByuserId(memberRequestDto.userId)
        if (member != null){
            throw InvaliduserIdException(fieldName = "userId", massage = "이미 가입한 사용자 아이디입니다!")
        }

        member = memberRequestDto.toEntity()
        memberRepository.save(member)

        val memberRole = MemberRole(
            id = null,
            role = Role.MEMBER,
            member = member,
        )
        memberRoleRepository.save(memberRole)

        return "회원가입이 완료되었습니다."
    }

    fun login(loginDto: LoginDto): TokenInfo {
        val authenticationToken = UsernamePasswordAuthenticationToken(loginDto.userId, loginDto.password)
        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)
        val accessToken = jwtTokenProvider.createAccessToken(authentication)
        return TokenInfo(grantType = "Bearer", accessToken = accessToken)
    }
}
