package com.example.mytube.video.model.entity;

import com.example.mytube.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor @AllArgsConstructor
@Table(name = "video")
public class Video extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long user_id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private VideoStatus status;
    private String file_url;
    private String thumbnail_url;
    private Long views;
}
