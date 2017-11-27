package xyz.sky731.programming.lab3;

import java.util.Objects;

class Crystals {
    private int count = 0;

    protected void addCrystal() {
        count++;
    }

    protected void useCrystal() {
        count--;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Crystals)) return false;

        Crystals crystals = (Crystals) o;
        if (count != crystals.count) return false;
        return true;
    }

    @Override
    public String toString() {
        return count + " кристаллов";
    }

    @Override
    public int hashCode() {
        return count;
    }
}
