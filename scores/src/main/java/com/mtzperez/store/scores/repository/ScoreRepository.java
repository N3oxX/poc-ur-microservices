package com.mtzperez.store.scores.repository;

import com.mtzperez.store.scores.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    public List<Score> findByStyleIdOrderByIdAsc(Long styleId);
}
