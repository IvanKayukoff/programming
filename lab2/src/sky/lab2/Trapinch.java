package sky.lab2;

import ru.ifmo.se.pokemon.*;

import java.lang.management.MonitorInfo;

public class Trapinch extends Pokemon {
    Trapinch() {
        super("Trapinch", 1);
        setStats(45 ,100, 45, 45, 45, 10);
        setType(Type.GROUND);

        Move[] moves = new Move[2];
        moves[0] = new DoubleTeam();
        moves[1] = new Confide();
        setMove(moves);
    }

    Trapinch (String name, int level) {
        super(name, level);
    }
}

/** Double Team raises the user's Evasiveness by one stage, thus making the user more
 *  difficult to hit.
 **/
class DoubleTeam extends StatusMove {
    DoubleTeam() {
        super(Type.NORMAL, 0 ,0);
    }
}

/** Confide lowers the target's Special Attack by one stage. */
class Confide extends StatusMove {
    Confide() {
        super(Type.NORMAL, 0, 0);
    }
}
