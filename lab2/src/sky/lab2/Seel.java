package sky.lab2;

import ru.ifmo.se.pokemon.*;

public class Seel extends Pokemon {
    public Seel(String name) {
        super(name, 47);
        setStats(65, 45, 55, 45, 70, 45);
        setType(Type.WATER);

        Move[] moves = new Move[3];
        moves[0] = new Waterfall();
        moves[1] = new AquaRing();
        moves[2] = new IceBeam();
        setMove(moves);
    }

    protected Seel(String name, int level) {
        super(name, level);
    }
}

class Waterfall extends PhysicalMove {
    public Waterfall() {
        super(Type.WATER, 80, 1);
    }
}

class AquaRing extends StatusMove {
    public AquaRing() {
        super(Type.WATER, 0 ,1);
    }
}

class IceBeam extends SpecialMove {
    public IceBeam() {
        super(Type.ICE, 90, 1);
    }
}