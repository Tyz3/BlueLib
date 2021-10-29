package ru.kronos.gamephase.regular;

import ru.kronos.gamephase.GamePhase;

public abstract class RegularRule {

    public void register(GamePhase gamePhase) {
        gamePhase.setRegularRule(this);
    }

}
