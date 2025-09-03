package com.example.mytube.video.service;

import com.example.mytube.video.repository.EncodingJobsRepository;
import com.example.mytube.video.repository.VideoRepository;
import com.example.mytube.video.util.AwsMediaConvertUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EncodingService {
    private final VideoRepository videoRepository;
    private final EncodingJobsRepository encodingJobsRepository;
    private final AwsMediaConvertUtil awsMediaConvertUtil;

    public String encode() {
        String jobId = awsMediaConvertUtil.beginJob();
        return jobId;
    }
}
