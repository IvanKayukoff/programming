package xyz.sky731.programming.lab3;

import java.util.ArrayList;
import java.util.List;

public class Human implements Employable {
    private int money = 0;
    private TypeHuman type = TypeHuman.WORKMAN;
    private List<Building> buildings = null;

    @Override
    public void work() {
        System.out.println("I'm working..");
    }

    public TypeHuman getType() {
        return type;
    }
    public Human(Home home) {
        buildings = new ArrayList<Building>();
        buildings.add(home);
    }

    public boolean buyBuilding(Building building, Human oldOwner) {
        if (money >= building.getCost()) {
            money -= building.getCost();
            buildings.add(building);
            oldOwner.sellBuilding(building);
            return true;
        } else {
            return false;
        }
    }

    private void sellBuilding(Building building) {
        buildings.remove(building);
        money += building.getCost();
    }
}
