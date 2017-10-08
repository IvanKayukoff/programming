package testing;

import ru.ifmo.se.pokemon.Battle;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Stat;
import sky.lab2.*;

public class Main {

    public static void main(String[] args) {
        Battle battle = new Battle();

        battle.addAlly(new Sigilyph("sig1"));
        battle.addAlly(new Flygon("fly1"));
        battle.addAlly(new Vibrava("vib1"));

        battle.addFoe(new Trapinch("trap1"));
        battle.addFoe(new Dewgong("dew1"));
        battle.addFoe(new Seel("seel1"));

        battle.go();
    }
}
