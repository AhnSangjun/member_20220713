package com.its.member;

import com.its.member.dto.BoardDTO;
import com.its.member.dto.MemberDTO;
import com.its.member.service.BoardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@SpringBootTest
public class BoardTest {
    @Autowired
    private BoardService boardService;

    public BoardDTO newBoard(int i) {
        BoardDTO board =
                new BoardDTO("title"+i, "writer"+i, "password"+i, "contents"+i);
        return board;
    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void saveTest() {
        IntStream.rangeClosed(1,20).forEach(i -> {
            boardService.save(newBoard(i));
        });
    }


}
