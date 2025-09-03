package com.example.mytube.video.util;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.mediaconvert.MediaConvertClient;
import software.amazon.awssdk.services.mediaconvert.model.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class AwsMediaConvertUtil {
    // AWS 자격 증명 설정

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKeyId;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretAccessKey;

//    @Value("${cloud.aws.region.static}")
//    private String region;

    // 입력 파일 버킷

    // s3://bbages/media/BigBuckBunny.mp4

    @Value("${input.s3.url}")
    private String inputS3Path;

    @Value("${input.s3.fileName}")
    private String inputFileName;

    // 출력 파일 버킷

    @Value("${output.s3.url}")
    private String outputS3Path;

    //mediaConvert

//    @Value("${mediaconvert.endpoint}")
//    private String mcEndPoint;

    @Value("${aws.roleARN}")
    private String roleARN;

    MediaConvertClient mediaConvertClient;

    //권한 주입
    @PostConstruct
    public void init() {
        mediaConvertClient = MediaConvertClient.builder()
                .region(Region.AP_NORTHEAST_2) // 서울 리전
                .build();
    }

    public String beginJob() {
        String jobId = UUID.randomUUID().toString();

        JobSettings settings = createHlsJobSetting(jobId);

        CreateJobRequest jobRequest = CreateJobRequest.builder()
                .role(roleARN)
                .settings(settings)
                .build();

        CreateJobResponse response = mediaConvertClient.createJob(jobRequest);

        log.info("🎬 MediaConvert Job 생성됨: {}", response.job().id());
        return response.job().id();
    }

    private JobSettings createHlsJobSetting(String jobId) {
        String inputPath = inputS3Path + inputFileName;
        String outputPath = outputS3Path + "/" + jobId + "/";

        Input input = Input.builder()
                .fileInput(inputPath)
                .audioSelectors(Map.of(
                        "Audio Selector 1",
                                AudioSelector.builder()
                                .defaultSelection(AudioDefaultSelection.DEFAULT)
                                .build()
                ))
                .build();

        // 해상도별 출력 정의
        Output hls360p = createHlsOutput(640, 360, 800_000, "_360p");
        Output hls720p = createHlsOutput(1280, 720, 2500_000, "_720p");
        Output hls1080p = createHlsOutput(1920, 1080, 5000_000, "_1080p");

        HlsGroupSettings hlsGroupSettings = HlsGroupSettings.builder()
                .destination(outputPath)
                .segmentLength(10)
                .minSegmentLength(2)
                .directoryStructure(HlsDirectoryStructure.SINGLE_DIRECTORY)
                .manifestDurationFormat(HlsManifestDurationFormat.INTEGER)
                .segmentControl(HlsSegmentControl.SEGMENTED_FILES)
                .outputSelection(HlsOutputSelection.MANIFESTS_AND_SEGMENTS)
                .clientCache(HlsClientCache.ENABLED)
                .build();

        OutputGroup hlsGroup = OutputGroup.builder()
                .name("HLS Group")
                .customName("hls")
                .outputGroupSettings(OutputGroupSettings.builder()
                        .type(OutputGroupType.HLS_GROUP_SETTINGS)
                        .hlsGroupSettings(hlsGroupSettings)
                        .build())
                .outputs(hls360p, hls720p, hls1080p)
                .build();

        return JobSettings.builder()
                .inputs(input)
                .outputGroups(hlsGroup)
                .timecodeConfig(TimecodeConfig.builder()
                        .source(TimecodeSource.EMBEDDED)
                        .build())
                .build();
    }

    private Output createHlsOutput(int width, int height, int bitrate, String suffix) {
        H264Settings h264Settings = H264Settings.builder()
                .rateControlMode(H264RateControlMode.VBR)
                .qualityTuningLevel(H264QualityTuningLevel.SINGLE_PASS)
                .codecLevel(H264CodecLevel.AUTO)
                .codecProfile(H264CodecProfile.MAIN)
                .maxBitrate(bitrate)
                .bitrate(bitrate)
                .build();

        VideoDescription videoDescription = VideoDescription.builder()
                .width(width)
                .height(height)
                .codecSettings(VideoCodecSettings.builder()
                        .codec(VideoCodec.H_264)
                        .h264Settings(h264Settings)
                        .build())
                .build();

        AudioDescription audioDescription = AudioDescription.builder()
                .codecSettings(AudioCodecSettings.builder()
                        .codec(AudioCodec.AAC)
                        .aacSettings(AacSettings.builder()
                                .bitrate(256_000)
                                .sampleRate(48_000)
                                .codingMode(AacCodingMode.CODING_MODE_2_0)
                                .build())
                        .build())
                .build();

        return Output.builder()
                .nameModifier(suffix)
                .containerSettings(ContainerSettings.builder()
                        .container(ContainerType.M3_U8)
                        .build())
                .videoDescription(videoDescription)
                .audioDescriptions(List.of(audioDescription))
                .build();
    }
}
