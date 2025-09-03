package com.example.mytube.video.model.entity;

import lombok.Getter;

@Getter
public enum EncodingStatus {
    PENDING, PROCESSING,COMPLETED, FAILED;
}
