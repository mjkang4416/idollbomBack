package com.example.idollbom.controller.customservice.question;

import com.example.idollbom.domain.dto.customservice.question.QuestionDTO;
import com.example.idollbom.domain.dto.customservice.question.QuestionListDTO;
import com.example.idollbom.domain.vo.ParentVO;
import com.example.idollbom.mapper.loginmapper.ParentMapper;
import com.example.idollbom.service.customserviceservice.question.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/ask")
@RequiredArgsConstructor
@Slf4j
public class AskController {

    private final ParentMapper parentMapper;
    private final QuestionService questionService;

    // 문의하기 페이지 불러오기
    @GetMapping("/list")
    public String ask(@RequestParam(value="pageNo", defaultValue = "1") int pageNo,
                      @RequestParam(value="pageSize", defaultValue = "3") int pageSize,
                      Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String currentUserName = userDetails.getUsername();

        ParentVO parent_info = parentMapper.selectOne(currentUserName);

        int totalQuestions = questionService.countQuestion();
        int totalPages = (int) Math.ceil((double) totalQuestions / pageSize);

        List<QuestionListDTO> questionList = questionService.findQuestionAll(pageNo, pageSize);


        for(QuestionListDTO question : questionList){
            log.info("공개/비공개 여부 : " + question.getQuestionReadingCheck().equals("비공개"));
            if(question.getQuestionReadingCheck().equals("비공개")){
                model.addAttribute("private", question.getQuestionReadingCheck());
            }else{
                model.addAttribute("public", question.getQuestionReadingCheck());
            }
        }

        int pageGroupSize = 5;
        int startPage = ((pageNo - 1) / pageGroupSize) * pageGroupSize + 1;
        int endPage = Math.min(startPage + pageGroupSize - 1, totalPages);

        model.addAttribute("parent_info", parent_info);
        model.addAttribute("questionList", questionList);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", totalPages);

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "/html/customerService/question/question";
    }

    @GetMapping("/write")
    public String goWriteForm(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String currentUserName = userDetails.getUsername();

        ParentVO parent_info = parentMapper.selectOne(currentUserName);

        model.addAttribute("parent_info", parent_info);
        model.addAttribute("question", new QuestionDTO());
        return "/html/customerService/question/inqueryBoardForm";
    }

    @PostMapping("/write")
    public String write(@ModelAttribute QuestionDTO question, RedirectAttributes redirectAttributes) {

        log.info("View에서 넘어온 데이터들 : ");
        log.info("questionReadingCheck(열람가능 여부) : " + question.getQuestionReadingCheck());
//        log.info("questionNumber(문의 pk) : " + question.getQuestionNumber());
        log.info("parentNumber(부모 pk) : " + question.getParentNumber());
        log.info("questionTitle(문의 제목) : " + question.getQuestionTitle());
        log.info("questionContent(문의 내용) : " + question.getQuestionContent());

        if(question.getQuestionReadingCheck().equals("비공개")){
            redirectAttributes.addFlashAttribute("private", "비공개");
        }else{
            redirectAttributes.addFlashAttribute("public", "공개");
        }

        // 문의하기 추가 쿼리문 실행
        questionService.saveQuestion(question);

        return "redirect:/ask/list";
    }
}