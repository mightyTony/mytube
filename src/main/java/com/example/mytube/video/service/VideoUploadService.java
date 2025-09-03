package com.example.mytube.video.service;

import com.example.mytube.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class VideoUploadService {
    private final VideoRepository videoRepository;
}
