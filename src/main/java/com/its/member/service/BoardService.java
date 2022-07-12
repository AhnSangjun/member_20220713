package com.its.member.service;

import com.its.member.common.PagingConst;
import com.its.member.dto.BoardDTO;
import com.its.member.entity.BoardEntity;
import com.its.member.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
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

    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getNumberOfPages();
        // 요청한 페이지가 1이면 페이지값을 0으로 하고 1이 아니면 요청 페이지에서 1을 뺀다.
//        page = page - 1;
        page = (page == 1)? 0: (page-1);
        Page<BoardEntity> boardEntities = boardRepository.findAll(PageRequest.of(page, PagingConst.PAGE_LIMIT, Sort.by(Sort.Direction.DESC, "id")));
        // Page<BoardEntity> => Page<BoardPagingDTO>
        Page<BoardDTO> boardList = boardEntities.map(
                board -> new BoardDTO(board.getId(),
                        board.getBoardTitle(),
                        board.getBoardWriter(),
                        board.getBoardHits(),
                        board.getCreatedTime()
                ));
                return boardList;
    }
}
