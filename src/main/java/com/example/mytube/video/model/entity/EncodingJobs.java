package com.example.mytube.video.model.entity;

import com.example.mytube.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor@AllArgsConstructor
@Table(name = "encoding_jobs")
public class EncodingJobs extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String videoTitle;
    private Long userId;
    private String resolution;
    @Enumerated(EnumType.STRING)
    private EncodingStatus status;
    @Column(name = "output_url")
    private String fileOutPutUrl;
    private String jobId;

    @Builder
    public EncodingJobs(String videoTitle, Long userId, String resolution, EncodingStatus status, String fileOutPutUrl, String jobId) {
        this.videoTitle = videoTitle;
        this.userId = userId;
        this.resolution = resolution;
        this.status = status;
        this.fileOutPutUrl = fileOutPutUrl;
        this.jobId = jobId;
    }


    public void updateStatus(EncodingStatus status) {
        this.status = status;
    }

    public void updateFileOutPutUrl(String fileOutPutUrl) {
        this.fileOutPutUrl = fileOutPutUrl;
    }
}
