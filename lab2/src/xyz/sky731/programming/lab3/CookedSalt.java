package xyz.sky731.programming.lab3;

import java.util.Objects;

public class CookedSalt {
    private int cost = 0;
    private int count = 0;
    public CookedSalt(int cost) {
        this.cost = cost;
    }

    public void decCost() {
        cost--;
    }

    public void cookSalt() {
        count++;
    }

    protected int sell() {
        int money = cost * count;
        count = 0;
        return money;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return count + " готовой соли по цене " + cost + " за каждую";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CookedSalt that = (CookedSalt) o;

        if (cost != that.cost) return false;
        if (count != that.count) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    protected void setCost(int cost) {
        this.cost = cost;
    }
}
