package com.example.mytube.video.repository;

import com.example.mytube.video.model.entity.EncodingJobs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EncodingJobsRepository extends JpaRepository<EncodingJobs, Long> {
}
