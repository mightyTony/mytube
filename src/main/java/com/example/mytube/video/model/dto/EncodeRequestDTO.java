package com.example.mytube.video.model.dto;

public record EncodeRequestDTO(
    Long userId,
    String title,
    String description,
    String originFileUrl
) {
}
