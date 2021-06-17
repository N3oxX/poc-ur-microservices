package com.mtzperez.store.scores.service;

import com.mtzperez.store.scores.entity.Score;

import java.util.List;

public interface ScoreService {
    public List<Score> listAllScores();
    public Score getScore(Long id);
    public Score createScore(Score score);
    public List<Score> createMultipleScores(List<Score> scores);
    public Score updateScore(Score score);
    public Score deleteScore(Long id);
    public List<Score> findByStyleId(Long styleId);
    public Score updateScorePoints(Long id, Double total, Boolean extra);
}
