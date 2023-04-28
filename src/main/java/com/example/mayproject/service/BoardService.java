package com.example.mayproject.service;

import com.example.mayproject.domain.Board;
import com.example.mayproject.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BoardService {

    @Autowired
    private BoardMapper mapper;

    public List<Board> listBoard() {
        List<Board> list = mapper.selectAll();
        return list;
    }

    public Board getBoard(Integer id) {
        return mapper.selectById(id);
    }

    public boolean modify(Board board) {
        int cnt = mapper.update(board);
        return cnt == 1;
    }

    public boolean remove(Integer id) {
        int cnt = mapper.deleteById(id);
        return cnt == 1;
    }

    public boolean createBoard(Board board) {
        return mapper.createBoard(board);
    }


    public Map<String, Object> listBoard(Integer page) {
        // TODO : 게시물 목록 넘겨주기, 페이지네이션이 필요한 정보 구해서 넘겨주기

        // 한 페이지 당 보여줄 게시물 수
        Integer rowPerPage = 10;

        // 쿼리절에 사용할 시작 인덱스
        Integer startIndex = (page - 1) * rowPerPage;

        // 페이지네이션이 필요한 정보 -----
        Integer numOfRecords = mapper.countAll(); // 전체 레코드의 개수
        // 마지막 페이지 번호
        Integer lastPageNumber = (numOfRecords - 1) / rowPerPage + 1;

        // 페이지네이션 왼쪽번호
        Integer leftPageNum = page - 5;
        // leftPageNum이 1보다 작으면 안됨
        leftPageNum = Math.max(leftPageNum, 1);

        // 페이지네이션 오른쪽번호
        Integer rightPageNum = leftPageNum + 9;
        // rightPageNum은 마지막페이지보다 크면 안됨
        rightPageNum = Math.min(rightPageNum, lastPageNumber);

        Map<String, Object> pageInfo = new HashMap<>();
        pageInfo.put("leftPageNum", leftPageNum);
        pageInfo.put("rightPageNum", rightPageNum);
        pageInfo.put("currentPageNum", page);
        pageInfo.put("lastPageNumber", lastPageNumber);

        // 게시물 목록 ------
        List<Board> list = mapper.selectAllPaging(startIndex, rowPerPage);

        return Map.of("pageInfo", pageInfo,
                    "boardList", list);
    }
}
