package com.example.mytube.video.entity;

import com.example.mytube.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor@AllArgsConstructor
@Table(name = "encoding_jobs")
public class EncodingJobs extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long video_id;
    private String resolution;
    @Enumerated(EnumType.STRING)
    private EncodingStatus status;
    @Column(name = "output_url")
    private String fileOutPutUrl;

}
