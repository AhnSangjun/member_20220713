package com.its.member.service;

import com.its.member.dto.MemberDTO;
import com.its.member.entity.MemberEntity;
import com.its.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    public final MemberRepository memberRepository;

    public Long save(MemberDTO memberDTO) {
//       memberRepository.save(MemberEntity.toSaveEntity(memberDTO));
        MemberEntity memberEntity = MemberEntity.toSaveEntity(memberDTO);
        Long savedId = memberRepository.save(memberEntity).getId();
        return savedId;
    }

    public MemberDTO login(MemberDTO memberDTO) {
        /**
         * login.html에서 입력받은 이메일, 비밀번호를 받아오고
         * DB로 부터 해당 이메일의 정보를 가져와서
         * 입력받은 비밀번호와 DB에서 조회한 비밀번호의 일치여부를 판단하여
         * 일치하면 로그인 성공, 일치하지 않은면 로그인 실패로 처리
         */
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
        if (optionalMemberEntity.isPresent()) {
           MemberEntity loginEntity = optionalMemberEntity.get();
           if (loginEntity.getMemberPassword().equals(memberDTO.getMemberPassword())) {
               return MemberDTO.toMemberDTO(loginEntity);
           } else {
               return null;
           }
        } else {
            return null;
        }
    }

    public MemberDTO findById(Long id) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
        if (optionalMemberEntity.isPresent()) {
//            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
            MemberEntity memberEntity = optionalMemberEntity.get();
            MemberDTO memberDTO = MemberDTO.toMemberDTO(memberEntity);
            return memberDTO;
        } else {
            return null;
        }
    }

}


















