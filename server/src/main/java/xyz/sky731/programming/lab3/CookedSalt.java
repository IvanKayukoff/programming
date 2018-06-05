package xyz.sky731.programming.lab3;

import java.util.Objects;

public class CookedSalt {
    private int count = 0;

    protected void addSalt() {
        count++;
    }

    protected int sell(int cost) {
        if (Market.buySalt(cost)) {
            int money = cost * count;
            count = 0;
            return money;
        } else return 0;
    }

    @Override
    public String toString() {
        return count + " готовой соли";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof CookedSalt)) return false;

        CookedSalt that = (CookedSalt) o;
        if (count != that.count) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return count;
    }
}
