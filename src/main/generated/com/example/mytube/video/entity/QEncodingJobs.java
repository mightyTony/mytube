package com.example.mytube.video.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QEncodingJobs is a Querydsl query type for EncodingJobs
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEncodingJobs extends EntityPathBase<EncodingJobs> {

    private static final long serialVersionUID = 2055799348L;

    public static final QEncodingJobs encodingJobs = new QEncodingJobs("encodingJobs");

    public final com.example.mytube.common.QBaseEntity _super = new com.example.mytube.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath fileOutPutUrl = createString("fileOutPutUrl");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath resolution = createString("resolution");

    public final EnumPath<EncodingStatus> status = createEnum("status", EncodingStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Long> video_id = createNumber("video_id", Long.class);

    public QEncodingJobs(String variable) {
        super(EncodingJobs.class, forVariable(variable));
    }

    public QEncodingJobs(Path<? extends EncodingJobs> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEncodingJobs(PathMetadata metadata) {
        super(EncodingJobs.class, metadata);
    }

}

