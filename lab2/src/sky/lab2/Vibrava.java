package sky.lab2;

import ru.ifmo.se.pokemon.*;

public class Vibrava extends Trapinch {
    Vibrava() {
        super("Vibrava", 29);
        setStats(50 ,70, 50, 50, 50, 70);
        setType(Type.GROUND, Type.DRAGON);

        Move[] moves = new Move[3];
        moves[0] = new Confide();
        moves[1] = new DoubleTeam();
        moves[2] = new BugBuzz();
        setMove(moves);
    }

    Vibrava(String name, int level) {
        super(name, level);
    }
}

/** Bug Buzz deals damage and has a 10% chance of lowering the
 *  target's Special Defense by one stage.
 **/
class BugBuzz extends SpecialMove {
    BugBuzz() {
        super(Type.BUG, 90 ,100);
    }

}