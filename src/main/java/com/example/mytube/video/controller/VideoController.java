package com.example.mytube.video.controller;

import com.example.mytube.video.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/video")
public class VideoController {
    private final VideoService videoService;


    /*
    - `GET /api/v1/videos/{id}` : 영상 메타데이터 조회 (제목, 설명, 썸네일, 업로드 날짜 등) // 아마 페이징 처리도 필요할 듯
    - `GET /api/v1/videos/{id}` : 영상 상세 조회 (메타데이터 + 스트리밍 URL)
    - `POST /api/v1/videos/{id}/like` : 좋아요
    - `POST /api/v1/videos/{id}/comments` : 댓글 작성
    - `GET /api/v1/videos/{id}/comments` : 댓글 목록 조회 (페이징 처리)
     */
}
