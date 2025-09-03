package com.example.mytube.video.controller;

import com.example.mytube.video.service.VideoUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/upload")
public class VideoUploadController {
    private final VideoUploadService uploadService;

    /*
    - `POST /api/v1/videos` : 영상 업로드 (multipart → 서버에서 S3/로컬 저장)
    - `GET /api/v1/videos/stream/{id}` : 영상 스트리밍 (HLS, MP4)
     */
}
