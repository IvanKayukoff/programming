package xyz.sky731.programming.lab3;

public abstract class Building {
    private int cost = 0;

    public Building(int cost) {
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Building building = (Building) o;

        if (cost != building.cost) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Здание стоимостью " + cost + " сантиков";
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
