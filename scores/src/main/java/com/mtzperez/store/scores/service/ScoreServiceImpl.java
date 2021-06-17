package com.mtzperez.store.scores.service;

import com.mtzperez.store.scores.entity.Score;
import com.mtzperez.store.scores.repository.ScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoreServiceImpl implements ScoreService {


    private final ScoreRepository scoreRepository;

    @Override
    public List<Score> listAllScores() {
        return scoreRepository.findAll();
    }



    @Override
    public Score getScore(Long id) {
        return scoreRepository.findById(id).orElse(null);
    }

    @Override
    public Score createScore(Score score) {
        score.setCreatedAt(new Date());
        score.setStatus("CREATED");
        return scoreRepository.save(score);
    }

    @Override
    public List<Score> createMultipleScores(List<Score> scores) {
        scoreRepository.saveAll(scores);
        return scores;
    }

    @Override
    public Score updateScore(Score score) {
        Score scoreDB = getScore(score.getId());
        if (null == scoreDB){
            return null;
        }
        scoreDB.setTotal(score.getTotal());
        scoreDB.setExtra(score.getExtra());
        return scoreRepository.save(scoreDB);
    }

    @Override
    public Score deleteScore(Long id) {
        Score scoreDB = getScore(id);
        if (null == scoreDB){
            return null;
        }
        scoreDB.setStatus("DELETED");
        return scoreRepository.save(scoreDB);
    }

    @Override
    public List<Score> findByStyleId(Long styleId) {
        return scoreRepository.findByStyleIdOrderByIdAsc(styleId);
    }


    @Override
    public Score updateScorePoints(Long id, Double total, Boolean extra) {
        Score scoreDB = getScore(id);
        if (null == scoreDB){
            return null;
        }
        scoreDB.setTotal(total);
        scoreDB.setExtra(extra);
        scoreDB.setUpdatedAt(new Date());
        return scoreRepository.save(scoreDB);
    }
}
