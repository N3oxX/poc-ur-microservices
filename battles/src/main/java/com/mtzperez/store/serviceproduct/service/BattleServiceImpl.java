package com.mtzperez.store.serviceproduct.service;


import com.mtzperez.store.serviceproduct.client.ScoreClient;
import com.mtzperez.store.serviceproduct.entity.Battle;
import com.mtzperez.store.serviceproduct.entity.Participant;
import com.mtzperez.store.serviceproduct.entity.Round;
import com.mtzperez.store.serviceproduct.entity.Style;
import com.mtzperez.store.serviceproduct.model.Score;
import com.mtzperez.store.serviceproduct.repository.BattleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BattleServiceImpl implements BattleService {

    @Autowired
    BattleRepository battleRepository;

    @Autowired
    ScoreClient scoreClient;

    @Override
    public List<Battle> findBattleAll() {
        List<Battle> battles = battleRepository.findAll();
        for (Battle battle :battles){
            getBattleScores(battle);
        }
       return  battles;
    }


    @Override
    public Battle createBattle(Battle battle) {
       Battle battleDB = battleRepository.findById(battle.getId()).orElse(null);
        if (battleDB !=null){
            return battleDB;
        }
        battle.setState("CREATED");
        battleDB = battleRepository.save(battle);

        for (Participant part : battleDB.getParticipants()) {
            for (Round round : part.getRounds()){
                for (Style sty : round.getStyles()){
                    List<Score> scoreAr = new ArrayList<>();
                    for(int i = 0; i< sty.getPatterns(); i++) {
                        Score sco = Score.builder()
                                .total(null)
                                .extra(false)
                                .status("CREATED")
                                .createdAt(new Date())
                                .styleId(sty.getId())
                                .build();
                        scoreAr.add(sco);
                    }
                    scoreClient.createMultipleScores(scoreAr);
                }
            }
        }
       return battleDB; //TODO GET THE SCORES OF THE OTHER MICROSERVICE
    }


    @Override
    public Battle updateBattle(Battle battle) {
        Battle battleDB = getBattle(battle.getId());
        if (battleDB == null){
            return  null;
        }
        battleDB.setName(battle.getName());
        return battle;
}


    @Override
    public Battle deleteBattle(Battle battle) {
        Battle battleDB = getBattle(battle.getId());
        if (battleDB == null){
            return  null;
        }
        battleDB.setState("DELETED");
        return battleRepository.save(battleDB);
    }

    @Override
    public Battle getBattle(Long id) {
        Battle battle = battleRepository.findById(id).orElse(null);
        assert battle != null;
        return getBattleScores(battle);
    }

    private Battle getBattleScores(Battle battle) {
        for (Participant part : battle.getParticipants()){
            for (Round round : part.getRounds()){
                    List<Style> listItem= round.getStyles().stream().map(battleItem -> {
                        if (null != battleItem){
                            battleItem.setScores(scoreClient.listScores(battleItem.getId()).getBody());
                        }
                        return battleItem;
                    }).collect(Collectors.toList());
                    round.setStyles(listItem);
            }
        }
        return battle;
    }
}
