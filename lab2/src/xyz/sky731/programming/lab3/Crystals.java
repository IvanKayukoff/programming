package xyz.sky731.programming.lab3;

import java.util.Objects;

public class Crystals {

    class ChangerCrystal {
        int a = 0;
        int b = 0;
        public ChangerCrystal(int a, int b) {
            this.a = a;
            this.b = b;
        }
        int add() {
            return a + b;
        }
    }

    private int count = 0;

    protected void addCrystal() {
        count = new ChangerCrystal(count, 1).add();
    }

    protected void useCrystal() {
    }

    public int getCount() {
        return count;
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
