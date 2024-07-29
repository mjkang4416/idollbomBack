package com.example.idollbom.service.myPageservice.parentservice;

import com.example.idollbom.domain.dto.myPagedto.parentdto.classSaveDTO;
import com.example.idollbom.domain.vo.ParentVO;
import com.example.idollbom.mapper.loginmapper.ParentMapper;
import com.example.idollbom.mapper.myPagemapper.parentmapper.classSaveMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
public class classSaveServiceImpl implements classSaveService {

    private final classSaveMapper classSaveMapper;
    private final ParentMapper parentMapper;

    @Override
    public List<classSaveDTO> selectClassList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String currentUserName = userDetails.getUsername();

//      parent VO 찾아서 아이디 찾기
        ParentVO parent = parentMapper.selectOne(currentUserName);

        return classSaveMapper.selectAll(parent.getParentNumber());
    }

    // 수업 찜 목록 추가
    @Override
    public int saveClass(Long classNumber, Long parentNumber) {
        return classSaveMapper.insertClassSave(classNumber, parentNumber);
    }

    // 수업 찜 목록 삭제 ( 찜 목록 버튼으로 삭제 )
    @Override
    public int deleteSaveClass(Long classNumber, Long parentNumber) {
        return classSaveMapper.deleteClassSave(classNumber, parentNumber);
    }

    @Override
    public int deleteClass(Long classNumber) {
        return classSaveMapper.deleteClass(classNumber);
    }
}
