package com.example.mytube.video.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class VideoRepositoryQueryImpl implements VideoRepositoryQuery {
    private final JPAQueryFactory queryFactory;
}
