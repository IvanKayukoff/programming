package sky.lab2;

import ru.ifmo.se.pokemon.Move;
import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Type;

public class Dewgong extends Seel {
    public Dewgong() {
        super("Dewgong", 47);
        setStats(90, 70, 80, 70, 95, 70);
        setType(Type.WATER, Type.ICE);

        Move[] moves = new Move[4];
        moves[0] = new Waterfall();
        moves[1] = new AquaRing();
        moves[2] = new IceBeam();
        moves[3] = new FrostBreath();
        setMove(moves);
    }
}

class FrostBreath extends SpecialMove {
    FrostBreath() {
        super(Type.ICE, 60, 90);
    }
}
