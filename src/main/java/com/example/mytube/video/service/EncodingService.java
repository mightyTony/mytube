package com.example.mytube.video.service;

import com.example.mytube.video.model.dto.EncodeRequestDTO;
import com.example.mytube.video.model.entity.EncodingJobs;
import com.example.mytube.video.model.entity.EncodingStatus;
import com.example.mytube.video.repository.EncodingJobsRepository;
import com.example.mytube.video.repository.VideoRepository;
import com.example.mytube.video.util.AwsMediaConvertUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EncodingService {
    private final VideoRepository videoRepository;
    private final EncodingJobsRepository encodingJobsRepository;
    private final AwsMediaConvertUtil awsMediaConvertUtil;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public String encode(EncodeRequestDTO requestDTO) {

        String jobId = awsMediaConvertUtil.beginJob(requestDTO);

        // DB에 인코딩 작업 기록 저장
        EncodingJobs jobs = EncodingJobs.builder()
                .userId(requestDTO.userId())
                .videoTitle(requestDTO.title())
                .resolution("HLS_360p_720p_1080p")
                .status(EncodingStatus.PROCESSING)
                .fileOutPutUrl(String.format("s3://%s/output/%s", bucket, jobId))
                .jobId(jobId)
                .build();

        encodingJobsRepository.save(jobs);

        return jobId;
    }


}
