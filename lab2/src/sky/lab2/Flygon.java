package sky.lab2;

import ru.ifmo.se.pokemon.Move;
import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Type;

public class Flygon extends Vibrava {
    Flygon() {
        super("Flygon", 1);
        setStats(80, 100, 80, 80, 80, 100);
        setType(Type.DRAGON, Type.GROUND);

        Move[] moves = new Move[4];
        moves[0] = new DoubleTeam();
        moves[1] = new Confide();
        moves[2] = new BugBuzz();
        moves[3] = new DragonBreath();
        setMove(moves);
    }
}

/** Dragon Breath deals damage and has a 30% chance of paralyzing the target. */
class DragonBreath extends SpecialMove {
    DragonBreath() {
        super(Type.DRAGON, 60, 100);
    }
}