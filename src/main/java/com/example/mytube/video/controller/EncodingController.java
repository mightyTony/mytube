package com.example.mytube.video.controller;

import com.example.mytube.common.ApiResponse;
import com.example.mytube.video.service.EncodingService;
import com.example.mytube.video.service.FileService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/upload")
public class EncodingController {
    private final EncodingService uploadService;
    private final FileService fileService;

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

    // presigned-url 발급 // 요청 받은 URL로 PUT 요청 보내서 업로드
    @GetMapping("/presigned-url/{fileName:.+}")
    public ApiResponse<Map<String, String>> getPresignedUrl(@PathVariable(name = "fileName")
                                                            @Schema(description = "파일 이름", example = "video.mp4")
                                                            String fileName) {
        return ApiResponse.success(fileService.getPresignedUrl("origin", fileName));
    }



}
