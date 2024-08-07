package com.example.idollbom.controller.myPage;

import com.example.idollbom.domain.dto.logindto.CustomUserDTO;
import com.example.idollbom.domain.dto.logindto.ParentDTO;
import com.example.idollbom.domain.dto.logindto.ProDTO;
import com.example.idollbom.domain.dto.myPagedto.parentdto.NoteListDTO;
import com.example.idollbom.domain.dto.prodto.proReportDTO;
import com.example.idollbom.domain.vo.ProPostVO;
import com.example.idollbom.domain.vo.ProVO;
import com.example.idollbom.domain.vo.classVO;
import com.example.idollbom.service.loginservice.ProService;
import com.example.idollbom.service.myPageservice.parentservice.noteService;
import com.example.idollbom.service.myPageservice.proservice.classService;
import com.example.idollbom.service.myPageservice.proservice.proUpdateService;
import com.example.idollbom.service.myPageservice.proservice.proselectService;
import com.example.idollbom.service.proService.ProDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/proMyPage")
@RequiredArgsConstructor
@Slf4j
public class proMypageController {
    private final classService classService;
    private final ProDetailService proDetailService;
    private final ProService proService;
    private final noteService noteService;
    private final proUpdateService proUpdateService;
    private final proselectService proselectService;
    
    // 전문가 pk, 이름, role을 넘기는 메서드
    public void getRole(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDTO) {

            CustomUserDTO pro = ((CustomUserDTO) authentication.getPrincipal());
            String proId = pro.getEmail();

            ProVO pro_info = proService.selectPro(proId);
            Long proNumber = pro_info.getProNumber();

            String role = pro_info.getRole();
            String proName = pro_info.getProName();

            System.out.println(proNumber);
            System.out.println(role);

            model.addAttribute("proName", proName);
            model.addAttribute("role", role);
            model.addAttribute("proNumber", proNumber);

        }
    }

    // 현재 로그인한 전문가 pk 가져오는 메소드
    public Long proNumber(){
        // 현재 로그인한 전문가 pk 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDTO p = ((CustomUserDTO) authentication.getPrincipal());
        String proId = p.getEmail();

        ProVO pro = proService.selectPro(proId);
        System.out.println(pro.getProNumber());

        return pro.getProNumber();
    }

//  내 수업목록 보기
    @GetMapping("/proClass")
    public String selectProClass(Model model){
        getRole(model);
        Long proId = proNumber();
        
        // 쪽지함 목록 카운트 (헤더에 보여줄 쪽지 목록 개수)
        int count = noteService.countProNoteList(proId);

        List<classVO> classVO = classService.selectMyClass(proId);
        ProVO proinfo = proselectService.selectPro();
        model.addAttribute("proInfo", proinfo);
        model.addAttribute("class",  classVO );
        model.addAttribute("count", count);
        return "html/myPage/pro/class";
    }

//  내 신고목록 보기
    @GetMapping("/report")
    public String selectReport(Model model){
        getRole(model);
        Long proId = proNumber();

        // 쪽지함 목록 카운트 (헤더에 보여줄 쪽지 목록 개수)
        int count = noteService.countProNoteList(proId);
        List<proReportDTO> proReportVOS = classService.selectReport(proId);
        ProVO proinfo = proselectService.selectPro();
        model.addAttribute("proInfo", proinfo);
        model.addAttribute("report",  proReportVOS );
        model.addAttribute("count", count);
        return "html/myPage/pro/hateUser";
    }

//  내 게시글 보기
    @GetMapping("/MyPost")
    public String selectMyPost(Model model){
        getRole(model);
        Long proId = proNumber();

        // 쪽지함 목록 카운트 (헤더에 보여줄 쪽지 목록 개수)
        int count = noteService.countProNoteList(proId);
        List<ProPostVO> proPostVOS = proDetailService.selectProPost(proId);
        ProVO proinfo = proselectService.selectPro();
        model.addAttribute("proInfo", proinfo);
        model.addAttribute("posts",  proPostVOS );
        model.addAttribute("count", count);
        return "html/myPage/pro/Post";
    }

// 내 프로필 보기
    @GetMapping("/profile")
    public String selectProfile(Model model){
        getRole(model);
        Long proId = proNumber();

        // 쪽지함 목록 카운트 (헤더에 보여줄 쪽지 목록 개수)
        int count = noteService.countProNoteList(proId);
        ProVO proPrivate = proDetailService.selectProPrivate(proId);
        ProVO proinfo = proselectService.selectPro();
        model.addAttribute("pro", proPrivate);
        model.addAttribute("count", count);
        model.addAttribute("proInfo", proinfo);
        return "html/myPage/pro/profile";
    }

    // 나의 쪽지 목록 보기
    @GetMapping("/myNote")
    public String selectNoteList(Model model) {

        getRole(model);
        Long proId = proNumber();

        // 쪽지함 목록 카운트 (헤더에 보여줄 쪽지 목록 개수)
        int count = noteService.countProNoteList(proId);

        // 전문가 쪽지 목록 조회
        List<NoteListDTO> noteList = noteService.findAllMyProNote(proId);
        ProVO proinfo = proselectService.selectPro();
        model.addAttribute("count", count);
        model.addAttribute("proInfo", proinfo);
        model.addAttribute("noteList", noteList);
        return "html/myPage/pro/mail";
    }

    // 캘린더
    @GetMapping("/calender")
    public String selectCalender(){
        return "html/myPage/pro/calender";
    }

    // 내 정보 수정
    @PostMapping("/update")
    public String updateInfo(@ModelAttribute ProDTO proDTO, @RequestParam("file") MultipartFile file){
        proUpdateService.update(proDTO , file);
        return "redirect:/proMyPage/profile";
    }
}
