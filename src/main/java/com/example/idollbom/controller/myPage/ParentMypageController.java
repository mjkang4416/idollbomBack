package com.example.idollbom.controller.myPage;

import com.example.idollbom.domain.dto.myPagedto.parentdto.kidDTO;
import com.example.idollbom.domain.vo.myPagevo.parentvo.askVO;
import com.example.idollbom.domain.vo.myPagevo.parentvo.kidVO;
import com.example.idollbom.domain.vo.myPagevo.parentvo.myPostVO;
import com.example.idollbom.domain.vo.myPagevo.parentvo.reportVO;
import com.example.idollbom.service.myPageservice.parentservice.askService;
import com.example.idollbom.service.myPageservice.parentservice.kidsService;
import com.example.idollbom.service.myPageservice.parentservice.myPostService;
import com.example.idollbom.service.myPageservice.parentservice.reportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ParentMyPage")
@RequiredArgsConstructor
@Slf4j
public class ParentMypageController {

    private final kidsService kidsService;
    private final reportService reportService;
    private final askService askService;
    private final myPostService myPostService;

//  kid 페이지로 이동
    @GetMapping("/kids")
    public String getKids(Model model){
        List<kidVO> kids = kidsService.selectKidsList();
        model.addAttribute("kid", new kidDTO());
        model.addAttribute("kids", kids);
        return "html/myPage/parent/myKids";
    }

//  아이등록
    @PostMapping("/insertKids")
    public String insertKids(@ModelAttribute kidDTO kid){
        log.info("HTML에서 넘어온 데이터: " + kid);

        //      날짜 문자열 뽑기
        LocalDate birthday = LocalDate.parse(kid.getChildAge());
        //      현재 날짜
        LocalDate currentDate = LocalDate.now();
        //      둘사이 기간
        Period period = Period.between(birthday, currentDate);
        //      년도만 뽑아서 저장
        int age = period.getYears();
        kid.setChildAge(String.valueOf(age));


        kidsService.insertKids(kid);

        return "redirect:/ParentMyPage/kids";


    }

    //  아이삭제
    @PostMapping("/deleteKids")
    public String deleteKids(@RequestBody Long kidNumber){
        log.info("받은거"+kidNumber);
        kidsService.deleteKids(kidNumber);
        return "redirect:/ParentMyPage/kids";
    }

    // 신고목록 페이지 이동
    @GetMapping("/reportPage")
    public String selectReport(Model model){
      List<reportVO> reportUser = reportService.selectReportList();
        model.addAttribute("reportUser", reportUser);
        return "html/myPage/parent/hateUser";
    }

    // 문의목록 페이지 이동
    @GetMapping("/askPage")
    public String selectAsk(Model model){
        List<askVO> askList = askService.selectAskList();
        model.addAttribute("askList",askList );
        return "html/myPage/parent/myAsk";
    }

    //내가쓴 글 페이지 이동
    @GetMapping("/myPost")
    public String selectMyPostList(Model model){
        List<myPostVO> postList = myPostService.selectPostList();
        model.addAttribute("myPost", postList);
        return "html/myPage/parent/myPost";
    }
}
