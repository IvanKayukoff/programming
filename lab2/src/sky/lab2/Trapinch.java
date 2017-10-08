package sky.lab2;

import ru.ifmo.se.pokemon.*;

import java.lang.management.MonitorInfo;

public class Trapinch extends Pokemon {
    public Trapinch(String name) {
        super(name, 1);
        setStats(45 ,100, 45, 45, 45, 10);
        setType(Type.GROUND);

        Move[] moves = new Move[2];
        moves[0] = new DoubleTeam();
        moves[1] = new Confide();
        setMove(moves);
    }

    protected Trapinch (String name, int level) {
        super(name, level);
    }
}

class DoubleTeam extends StatusMove {
    DoubleTeam() {
        super(Type.NORMAL, 0 ,1);
    }
}

class Confide extends StatusMove {
    Confide() {
        super(Type.NORMAL, 0, 1);
    }
}
