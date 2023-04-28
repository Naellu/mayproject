package com.example.mayproject.controller;

import com.example.mayproject.domain.Board;
import com.example.mayproject.mapper.BoardMapper;
import com.example.mayproject.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/")
public class BoardController {

    @Autowired
    private BoardService service;

    // 경로 : http://localhost:8080?page=3
    // 경로 : http://localhost:8080/list?page=5
    // 게시물 목록
    // @RequestMapping(value = {"/", "list"}, method = RequestMethod.GET)
    @GetMapping({"/", "/list"})
    public String list(Model model, @RequestParam(value = "page", defaultValue = "1") Integer page) {
        // 1. request param = 수집/가공
        // 2. business logic 처리
//        List<Board> list = service.listBoard(); // 페이지 처리 전
//        List<Board> list = service.listBoard(page); // 페이지 처리 후
        Map<String, Object> result = service.listBoard(page);// 페이지 처리 후

        // 3. add attribute
//        model.addAttribute("boardList", list);
//        log.info("board record counting = {}", list.size());
        model.addAttribute("boardList", result.get("boardList"));
        model.addAttribute("pageInfo", result.get("pageInfo"));

        // 4. forward/redirect
        return "list";
    }

    @GetMapping("/id/{id}")
    public String showContent(@PathVariable Integer id, Model model) {
        // 1. request param
        // 2. business logic
        Board board = service.getBoard(id);
        log.info("board={}", board);
        // 3. add attribute
        model.addAttribute("board", board);
        // 4. forward/redirect

        return "board-content";
    }

    @GetMapping("/modify/{id}")
    public String getModify(@PathVariable Integer id, Model model) {
        model.addAttribute("board", service.getBoard(id));
        return "modify-content";
    }

    @PostMapping("/modify/{id}")
    public String modifyProcess(Board board, RedirectAttributes rttr) {
        log.info("updated board={}", board);
        boolean ok = service.modify(board);

        if (ok) {
            // 해당 게시물 보기로 리디렉션
//            rttr.addAttribute("success", "success"); // rttr을 사용하면 쿼리스트링으로 넘어간다
            rttr.addAttribute("message", board.getId() + "번 게시물이 수정되었습니다");
            return "redirect:/id/" + board.getId();
        } else {
            // 수정 form으로 리디렉션
//            rttr.addAttribute("fail", "fail");
            rttr.addAttribute("message", board.getId() + "번 게시물이 수정되지 않았습니다");
            return "redirect:/modify/" + board.getId();
        }
    }

//    @PostMapping("/remove")
//    public String remove(Integer id) {
//        boolean ok = service.remove(id);
//        if (ok) {
//            log.info("deleted BoardId={}", id);
//            return "redirect:/list";
//        } else {
//            return "redirect:/id/" + service.getBoard(id);
//        }
//    }

    @PostMapping("/remove")
    public String remove(Integer id, RedirectAttributes rttr) {
        boolean ok = service.remove(id);
        if (ok) {
            log.info("deleted BoardId={}", id);

            rttr.addAttribute("message", id + "번 게시물이 삭제되었습니다");

            return "redirect:/list";
        } else {
            return "redirect:/id/" + service.getBoard(id);
        }
    }

    @GetMapping("/create")
    public String getNewForm(Model model) {
        log.info("create controller method");
        model.addAttribute("board", new Board());
        return "create-content";
    }

    @PostMapping("/create/new")
    public String createNewForm(Board board) {
        boolean createSuccess = service.createBoard(board);
        log.info("insert method is = {}",createSuccess);
//        return createSuccess ? "redirect:/id/" + service.getBoard(board.getId()) : "redirect:/create";
        return createSuccess ? "redirect:/" : "redirect:/create";
    }


}
