package com.mtzperez.store.serviceproduct.service;


import com.mtzperez.store.serviceproduct.entity.Battle;

import java.util.List;

public interface BattleService {
    public List<Battle> findBattleAll();

    public Battle createBattle(Battle battle);
    public Battle updateBattle(Battle battle);
    public Battle deleteBattle(Battle battle);

    public Battle getBattle(Long id);
}
