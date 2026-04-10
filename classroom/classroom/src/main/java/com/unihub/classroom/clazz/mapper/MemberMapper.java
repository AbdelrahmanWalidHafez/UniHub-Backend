package com.unihub.classroom.clazz.mapper;

import com.unihub.classroom.clazz.dto.MemberDto;
import com.unihub.classroom.clazz.model.Member;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    public MemberDto toDto(Member member) {
        MemberDto memberDto = new MemberDto();
        memberDto.setEmail(member.getEmail());
        memberDto.setRid(member.getRid());
        memberDto.setCreatedAt(member.getCreatedAt());
        memberDto.setUpdatedAt(member.getUpdatedAt());
        memberDto.setCreatedBy(member.getCreatedBy());
        memberDto.setUpdatedBy(member.getUpdatedBy());
        return memberDto;
    }
}
