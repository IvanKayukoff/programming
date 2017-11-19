package xyz.sky731.programming.lab3;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Human implements Ownable {
    private int money = 0;
    private List<Building> buildings = null;
    private String name = null;

    public Human(Home home, int money, String name) {
        buildings = new ArrayList<Building>();
        buildings.add(home);
        this.money = money;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void getSalary(int money) {
        this.money += money;
    }

    @Override
    public boolean buyBuilding(Building building) {
        if (building == null) return false;
        if (money >= building.getCost()) {
            money -= building.getCost();
            buildings.add(building);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void sellBuilding(Building building) {
        if (building == null) return;
        money += building.getCost();
        buildings.remove(building);
    }

    public boolean giveMoney(Factory factory, int money) {
        if (this.money >= money) {
            factory.addMoney(money);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Я - человек имеющий " + buildings.size() + " зданий";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Human human = (Human) o;

        if (money != human.money) return false;
        if (!buildings.equals(human.buildings)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
