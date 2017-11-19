package xyz.sky731.programming.lab3;

import java.util.Objects;

public class Crystals {
    private int cost = 0;
    private int count = 0;
    public Crystals(int cost) {
        this.cost = cost;
    }

    public void buyCrystal() {
        count++;
    }

    public int getCost() {
        return cost;
    }

    public void useCrystal() {
        count--;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Crystals crystals = (Crystals) o;

        if (cost != crystals.cost) return false;
        if (count != crystals.count) return false;

        return true;
    }

    @Override
    public String toString() {
        return count + " кристаллов по цене " + cost + " за каждый";
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
