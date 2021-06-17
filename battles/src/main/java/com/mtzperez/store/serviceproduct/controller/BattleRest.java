package com.mtzperez.store.serviceproduct.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtzperez.store.serviceproduct.entity.Battle;

import com.mtzperez.store.serviceproduct.service.BattleService;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@RequestMapping("/battles")
public class BattleRest {

    @Autowired
    BattleService battleService;

    // -------------------Retrieve All Battles--------------------------------------------
    @GetMapping
    public ResponseEntity<List<Battle>> listAllBattles() {
        List<Battle> styles = battleService.findBattleAll();
        if (styles.isEmpty()) {
            return  ResponseEntity.noContent().build();
        }
        return  ResponseEntity.ok(styles);
    }

    // -------------------Retrieve Single Battle------------------------------------------
    @GetMapping(value = "/{id}")
    public ResponseEntity<Battle> getBattle(@PathVariable("id") long id) {
        log.info("Fetching Battle with id {}", id);
        Battle style = battleService.getBattle(id);
        if (null == style) {
            log.error("Battle with id {} not found.", id);
            return  ResponseEntity.notFound().build();
        }
        return  ResponseEntity.ok(style);
    }



    // -------------------Create a Battle-------------------------------------------
    @PostMapping
    public ResponseEntity<Battle> createBattle(@Valid @RequestBody Battle battle, BindingResult result) {
        log.info("Creating Battle : {}", battle);
        if (result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }
        Battle battleDB = battleService.createBattle(battle);

        return  ResponseEntity.status( HttpStatus.CREATED).body(battleDB);
    }

    // ------------------- Update a Battle ------------------------------------------------
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateBattle(@PathVariable("id") long id, @RequestBody Battle battle) {
        log.info("Updating Battle with id {}", id);

        battle.setId(id);
        Battle currentBattle = battleService.updateBattle(battle);

        if (currentBattle == null) {
            log.error("Unable to update. Battle with id {} not found.", id);
            return  ResponseEntity.notFound().build();
        }
        return  ResponseEntity.ok(currentBattle);
    }

    // ------------------- Delete a Battle-----------------------------------------
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Battle> deleteBattle(@PathVariable("id") long id) {
        log.info("Fetching & Deleting Battle with id {}", id);

        Battle battle = battleService.getBattle(id);
        if (battle == null) {
            log.error("Unable to delete. Battle with id {} not found.", id);
            return  ResponseEntity.notFound().build();
        }
        battle = battleService.deleteBattle(battle);
        return ResponseEntity.ok(battle);
    }

    private String formatMessage( BindingResult result){
        List<Map<String,String>> errors = result.getFieldErrors().stream()
                .map(err ->{
                    Map<String,String> error =  new HashMap<>();
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
