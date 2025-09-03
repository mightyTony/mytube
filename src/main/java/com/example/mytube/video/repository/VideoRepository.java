package com.example.mytube.video.repository;

import com.example.mytube.video.model.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long>, VideoRepositoryQuery {
}
