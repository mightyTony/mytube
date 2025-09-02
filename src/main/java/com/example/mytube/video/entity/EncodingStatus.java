package com.example.mytube.video.entity;

import lombok.Getter;

@Getter
public enum EncodingStatus {
    PENDING, PROCESSING,COMPLETED, FAILED;
}
