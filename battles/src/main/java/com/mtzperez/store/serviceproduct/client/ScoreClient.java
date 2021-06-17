package com.mtzperez.store.serviceproduct.client;

import com.mtzperez.store.serviceproduct.model.Score;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "scores")
@RequestMapping(value = "/scores")
public interface ScoreClient {

    @GetMapping
    public ResponseEntity<List<Score>> listScores(@RequestParam (name = "styleId" , required = false) Long styleId);

    @PostMapping(value = "/multiple")
    public ResponseEntity<List<Score>> createMultipleScores (@RequestBody List<Score> score);
}
