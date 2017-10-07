package sky.lab2;

import ru.ifmo.se.pokemon.*;

public class Seel extends Pokemon {
    public Seel() {
        super("Seel", 47);
        setStats(65, 45, 55, 45, 70, 45);
        setType(Type.WATER);

        Move[] moves = new Move[3];
        moves[0] = new Waterfall();
        moves[1] = new AquaRing();
        moves[2] = new IceBeam();
        setMove(moves);
    }

    public Seel(String name, int level) {
        super(name, level);
    }
}


class Waterfall extends PhysicalMove {
    public Waterfall() {
        super(Type.WATER, 80, 100);
    }
}

class AquaRing extends StatusMove {
    public AquaRing() {

    }
}

class IceBeam extends SpecialMove {
    /** Ice Beam deals damage and has a 10% chance of freezing the target. */
    public IceBeam() {
        super(Type.ICE, 90, 100);
    }

    @Override
    protected void applyOppEffects(Pokemon p) {
        if (Math.random() < 0.1) {
            Effect.freeze(p);
        }
    }
}