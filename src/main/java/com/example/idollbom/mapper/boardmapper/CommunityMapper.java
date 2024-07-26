package com.example.idollbom.mapper.boardmapper;

import com.example.idollbom.domain.dto.boarddto.CommunityDTO;
import com.example.idollbom.domain.dto.boarddto.CommunityDetailDTO;
import com.example.idollbom.domain.dto.boarddto.CommunityListDTO;
import com.example.idollbom.domain.dto.recommend.PagedResponse;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Mapper
public interface CommunityMapper {

    //게시글 목록
    List<CommunityListDTO> selectAll(int startRow, int endRow);

    // 페이징 처리 시 사용
    int countCommunity();

//    게시글 작성때 사용할 쿼리
    int getSeq();
//    게시글 작성
    void saveCommunity(CommunityDTO community);

    // 게시글 상세보기
    CommunityDetailDTO selectCommunityDetail(Long parentPostNumber);

    // 게시글 삭제하기
    void deleteCommunity(Long parentPostNumber);

    // 게시글 수정하기
    void updateCommunity(CommunityDTO community);

    // 조회수 SELECT
    void plusView(Long parentPostNumber);

    // 게시글 검색 기능 구현
    List<CommunityListDTO> searchCommunityList(String searchType, String searchWord, int startRow, int endRow);

    // 게시글 검색 시 갯수
    int countSearchCommunity(String searchType, String searchWord);
}


