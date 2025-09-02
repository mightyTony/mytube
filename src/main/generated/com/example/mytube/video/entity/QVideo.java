package com.example.mytube.video.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QVideo is a Querydsl query type for Video
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QVideo extends EntityPathBase<Video> {

    private static final long serialVersionUID = -1224822544L;

    public static final QVideo video = new QVideo("video");

    public final com.example.mytube.common.QBaseEntity _super = new com.example.mytube.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final StringPath file_url = createString("file_url");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<VideoStatus> status = createEnum("status", VideoStatus.class);

    public final StringPath thumbnail_url = createString("thumbnail_url");

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Long> user_id = createNumber("user_id", Long.class);

    public final NumberPath<Long> views = createNumber("views", Long.class);

    public QVideo(String variable) {
        super(Video.class, forVariable(variable));
    }

    public QVideo(Path<? extends Video> path) {
        super(path.getType(), path.getMetadata());
    }

    public QVideo(PathMetadata metadata) {
        super(Video.class, metadata);
    }

}

