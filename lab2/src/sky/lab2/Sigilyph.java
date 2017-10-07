package sky.lab2;

import ru.ifmo.se.pokemon.*;

public class Sigilyph extends Pokemon {

    public Sigilyph() {
        super("Sigilyph", 18);
        setStats(72, 58, 80, 103, 80, 97);
        setType(Type.FLYING, Type.PSYCHIC);

        Move[] moves = new Move[4];
        moves[0] = new DreamEater();
        moves[1] = new FlashCannon();
        moves[2] = new Psybeam();
        moves[3] = new Roost();
        setMove(moves);
    }
}

class DreamEater extends PhysicalMove {
    public DreamEater() {
        super(Type.PSYCHIC, 100, 100);
    }
}

class FlashCannon extends SpecialMove {
    public FlashCannon() {
        super(Type.STEEL, 80, 100);
    }
}

class Psybeam extends SpecialMove {
    public Psybeam() {
        super(Type.PSYCHIC, 65, 100);
    }
}

class Roost extends StatusMove {
    public Roost() {
        super();
    }
}