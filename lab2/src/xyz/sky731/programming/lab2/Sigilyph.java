package xyz.sky731.programming.lab2;

import ru.ifmo.se.pokemon.*;

public class Sigilyph extends Pokemon {

    public Sigilyph(String name) {
        super(name, 18);
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
        super(Type.PSYCHIC, 100, 1);
    }

    @Override
    protected String describe() {
        return "DreamEater";
    }
}

class FlashCannon extends SpecialMove {
    public FlashCannon() {
        super(Type.STEEL, 80, 1);
    }
    @Override
    protected String describe() {
        return "FlashCannon";
    }
}

class Psybeam extends SpecialMove {
    public Psybeam() {
        super(Type.PSYCHIC, 65, 1);
    }

    @Override
    protected String describe() {
        return "Psybeam";
    }
}

class Roost extends StatusMove {
    public Roost() {
        super(Type.FLYING, 0, 1);
    }

    @Override
    protected String describe() {
        return "Roost";
    }
}