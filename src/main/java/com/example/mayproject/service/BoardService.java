package com.example.mayproject.service;

import com.example.mayproject.domain.Board;
import com.example.mayproject.mapper.BoardMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
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


    public Map<String, Object> listBoard(Integer page, String search, String condition) {
        // TODO : 게시물 목록 넘겨주기, 페이지네이션이 필요한 정보 구해서 넘겨주기

        // 한 페이지 당 보여줄 게시물 수
        Integer rowPerPage = 10;

        // 쿼리절에 사용할 시작 인덱스
        Integer startIndex = (page - 1) * rowPerPage;

        // 페이지네이션이 필요한 정보 -----
        //  String condition = "writer"; // 검색조건 샘플 추가
        //  Integer numOfRecords = mapper.countAll(); // 전체 레코드의 개수
        //  Integer numOfRecords = mapper.countAll(search); // 전체 레코드의 개수
        Integer numOfRecords = mapper.countAllWithCondition(search, condition); // 전체 레코드의 개수


        // 마지막 페이지 번호
        Integer lastPageNumber = (numOfRecords - 1) / rowPerPage + 1;

        // 페이지네이션 왼쪽번호
        // Integer leftPageNum = page - 5;

        // 네비게이션 범위를 넘기전까지(현재 페이지가 rightPageNum보다 큰 값을 가지기전까지) 왼쪽번호 고정
        Integer leftPageNum = ((page - 1) / 10) * 10 + 1;

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
        List<Board> list = mapper.selectAllPagingWithCondition(startIndex, rowPerPage, search, condition);

        return Map.of("pageInfo", pageInfo,
                    "boardList", list);
    }
}
