package com.example.mytube.video.controller;

import com.example.mytube.common.ApiResponse;
import com.example.mytube.video.service.EncodingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/upload")
public class EncodingController {
    private final EncodingService uploadService;

    /*
    - `POST /api/v1/videos` : 원본 영상 업로드 (presigned Url -> S3/로컬 저장)
    - `GET /api/v1/videos/stream/{id}` : 영상 스트리밍 (HLS, MP4)
     */

    // 여러 해상도 인코딩 요청
//    @PostMapping("/{videoId}/encode")
//    public ApiResponse<Void> encode(@PathVariable Long videoId) {
//        uploadService.e
//        return ApiResponse.success();
//    }





}
