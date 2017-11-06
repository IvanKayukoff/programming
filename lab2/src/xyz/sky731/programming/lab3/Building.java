package xyz.sky731.programming.lab3;

public abstract class Building {
    private int cost = 0;

    public Building(int cost) {
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }
}
