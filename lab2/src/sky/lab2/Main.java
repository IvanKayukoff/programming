package sky.lab2;

import ru.ifmo.se.pokemon.Battle;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Stat;

public class Main {

    public static void main(String[] args) {
        Battle battle = new Battle();

        battle.addAlly(new Sigilyph());
        battle.addAlly(new Flygon());
        battle.addAlly(new Vibrava());

        battle.addFoe(new Trapinch());
        battle.addFoe(new Dewgong());

        battle.go();
    }
}
