package com.example.mytube.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

@Slf4j
@Component
public class FfmpegUtil {

    private static final String FFMPEG_PATH = "/usr/local/bin/ffmpeg"; // Mac 기준

    public void executeCommand(String command) throws Exception {
        ProcessBuilder pb = new ProcessBuilder("/bin/sh", "-c", command);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                log.info("[FFmpeg] {}", line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("FFmpeg 실행 실패. 종료 코드: " + exitCode);
        }
    }

    public void generateHlsAndThumbnail(String inputPath, String outputDir) throws Exception {
        File dir = new File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 1) 멀티 해상도 HLS 변환
        String hlsCmd = String.join(" ",
                FFMPEG_PATH, "-i", inputPath,
                // 480p
                "-filter:v:0 scale=-2:480 -c:v h264 -b:v:0 800k -c:a aac -strict -2 " +
                        "-hls_time 6 -hls_segment_filename " + outputDir + "/480p_%03d.ts " +
                        "-hls_playlist_type vod " + outputDir + "/480p.m3u8",
                // 720p
                "-filter:v:1 scale=-2:720 -c:v h264 -b:v:1 1500k -c:a aac -strict -2 " +
                        "-hls_time 6 -hls_segment_filename " + outputDir + "/720p_%03d.ts " +
                        "-hls_playlist_type vod " + outputDir + "/720p.m3u8",
                // 1080p
                "-filter:v:2 scale=-2:1080 -c:v h264 -b:v:2 3000k -c:a aac -strict -2 " +
                        "-hls_time 6 -hls_segment_filename " + outputDir + "/1080p_%03d.ts " +
                        "-hls_playlist_type vod " + outputDir + "/1080p.m3u8"
        );
        executeCommand(hlsCmd);

        // 2) 썸네일 생성
        String thumbCmd = String.format(
                "%s -i %s -ss 00:00:10 -vframes 1 %s/thumbnail.jpg",
                FFMPEG_PATH, inputPath, outputDir
        );
        executeCommand(thumbCmd);

        // 3) master.m3u8 수동 생성
        String master = "#EXTM3U\n" +
                "#EXT-X-STREAM-INF:BANDWIDTH=800000,RESOLUTION=854x480\n480p.m3u8\n" +
                "#EXT-X-STREAM-INF:BANDWIDTH=1500000,RESOLUTION=1280x720\n720p.m3u8\n" +
                "#EXT-X-STREAM-INF:BANDWIDTH=3000000,RESOLUTION=1920x1080\n1080p.m3u8\n";

        java.nio.file.Files.writeString(new File(outputDir, "master.m3u8").toPath(), master);
    }
}