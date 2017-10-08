package sky.lab2;

import ru.ifmo.se.pokemon.*;

public class Vibrava extends Trapinch {
    public Vibrava(String name) {
        super(name, 29);
        setStats(50 ,70, 50, 50, 50, 70);
        setType(Type.GROUND, Type.DRAGON);

        Move[] moves = new Move[3];
        moves[0] = new Confide();
        moves[1] = new DoubleTeam();
        moves[2] = new BugBuzz();
        setMove(moves);
    }

    protected Vibrava(String name, int level) {
        super(name, level);
    }
}

class BugBuzz extends SpecialMove {
    BugBuzz() {
        super(Type.BUG, 90, 1);
    }
}