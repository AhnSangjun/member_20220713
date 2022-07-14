package com.its.member.repository;

import com.its.member.dto.BoardMapperDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapperRepository {
    List<BoardMapperDTO> boardList();

    void save(BoardMapperDTO boardMapperDTO);
}
