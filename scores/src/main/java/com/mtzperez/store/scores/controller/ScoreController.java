package com.mtzperez.store.scores.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtzperez.store.scores.entity.Score;
import com.mtzperez.store.scores.service.ScoreService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/scores")
public class ScoreController {

    Logger logger = LogManager.getLogger(ScoreController.class);

    @Autowired
    private ScoreService scoreService;

    @GetMapping
    public ResponseEntity<List<Score>> listScores( @RequestParam (name = "styleId" , required = false) Long styleId) {

        List<Score> scores;
        if (null == styleId){
            scores = scoreService.listAllScores();
            if (scores.isEmpty()){
                return ResponseEntity.noContent().build();
            }
        }else{
            scores = scoreService.findByStyleId(styleId);
            if (scores == null){
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.ok(scores);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<Score> getScore (@PathVariable("id") Long id){
        logger.info("Fetching score with id {}", id);
        Score score = scoreService.getScore(id);
        if( score == null) {
            logger.info("score with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
            return ResponseEntity.ok(score);

    }
    @PostMapping
    public ResponseEntity<Score> createScore (@Valid @RequestBody Score score, BindingResult result) {
        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }
        Score scoreCreation = scoreService.createScore(score);
        return ResponseEntity.status(HttpStatus.CREATED).body(scoreCreation);
    }

    @PostMapping (value = "/multiple")
    public ResponseEntity<List<Score>> createMultipleScores (@RequestBody List<Score> score) {
        List<Score> scoreCreation = scoreService.createMultipleScores(score);
        return ResponseEntity.status(HttpStatus.CREATED).body(scoreCreation);
    }

    @PutMapping (value = "/{id}")
    public ResponseEntity<Score> updateScore (@PathVariable("id") Long id, @RequestBody Score score) {
        score.setId(id);
        Score scoreDB = scoreService.updateScore(score);
        if (scoreDB == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(scoreDB);
    }

    @PutMapping (value = "/{id}/points")
    public ResponseEntity<Score> updateScorePoints (@PathVariable("id") Long id, @RequestParam Double total, @RequestParam Boolean extra) {
        Score scoreDB = scoreService.updateScorePoints(id, total, extra);
        if (scoreDB == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(scoreDB);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Score> deleteScore (@PathVariable Long id) {
        Score scoreDelete = scoreService.deleteScore(id);

        if ( scoreDelete == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(scoreDelete);
    }

    private String formatMessage( BindingResult result){
        List<Map<String,String>> errors = result.getFieldErrors().stream()
                .map(err ->{
                    Map<String,String>  error =  new HashMap<>();
                    error.put(err.getField(), err.getDefaultMessage());
                    return error;

                }).collect(Collectors.toList());
        ErrorMessage errorMessage = ErrorMessage.builder()
                .code("01")
                .messages(errors).build();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString="";
        try {
            jsonString = mapper.writeValueAsString(errorMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

}
