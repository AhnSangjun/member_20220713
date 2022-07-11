package com.its.member.service;

import com.its.member.dto.BoardDTO;
import com.its.member.entity.BoardEntity;
import com.its.member.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public Long save(BoardDTO boardDTO) {
       Long savedId = boardRepository.save(BoardEntity.toSaveEntity(boardDTO)).getId();
       return savedId;
    }

    public List<BoardDTO> findAll() {
       List<BoardEntity> boardEntityList = boardRepository.findAll();
       List<BoardDTO> boardDTOList = new ArrayList<>();
       for (BoardEntity boardEntity: boardEntityList) {
           boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
       }
       return boardDTOList;
    }

    @Transactional
    public BoardDTO findById(Long id) {
        // 조회수 처리
        // native sql: update board_table set boardHits+1 where id=?
        boardRepository.boardHits(id);
      Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
      if (optionalBoardEntity.isPresent())  {
          return BoardDTO.toBoardDTO(optionalBoardEntity.get());
      } else {
          return null;
      }
    }

    public void update(BoardDTO boardDTO) {
        boardRepository.save(BoardEntity.toUpdateEntity(boardDTO));
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }
}
