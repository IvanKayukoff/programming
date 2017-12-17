package xyz.sky731.programming.lab3;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Human implements Ownable {
    private int money = 1000;
    private List<Building> buildings = null;
    private String name = null;
    private static int number = 0;

    public Human(String name, int money) {
        this.money = money;
        buildings = new ArrayList<Building>();
        buildings.add(new Home());
        this.name = name;
        number++;
    }

    public Human() {
        buildings = new ArrayList<Building>();
        buildings.add(new Home());
        this.name = "Human" + number++;
    }

    public boolean isBig() {
        for (Building building: buildings) {
            if (building instanceof Factory) {
                Factory factory = (Factory) building;
                if (factory.isBig()) return true;
            }
        }
        return false;
    }

    protected void think() {
        System.out.print(name + " думает: ");
        if (Market.getAvgSaltCost() > 10) {
            System.out.println("\"Все хорошо, я богатею..\"");
        } else {
            if (isBig()) {
                System.out.println("\"Слишком много мелких заводов..\"");
            } else {
                System.out.println("\"Зачем им так много соли?..\"");
            }

        }
    }

    public String getName() {
        return name;
    }

    public Home getHome() {
        for (int i = 0; i < buildings.size(); i++) {
            if (buildings.get(i) instanceof Home) return (Home) buildings.get(i);
        }
        return null;
    }

    public int getMoney() {
        return money;
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

    public void addBuildings(Building... buildings) {
        for (Building building: buildings) {
            if (building == null) continue;
            this.buildings.add(building);
        }
    }

    @Override
    public void sellBuilding(Building building) {
        if (building == null) return;
        money += building.getCost();
        buildings.remove(building);
        if (building instanceof Home) System.out.println(name + " продал свой дом");
    }

    public boolean giveMoney(Factory factory, int money) {
        if (factory == null) return false;
        if (money < 0) return false;
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
        if (o == null || !(o instanceof Human)) return false;

        Human human = (Human) o;
        if (money != human.money) return false;
        if (!buildings.equals(human.buildings)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = money;
        result = 31 * result + (buildings != null ? buildings.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        if (buildings != null || buildings.size() > 0) {
            for (Building building : buildings) {
                result = 31 * result + (building != null ? building.hashCode() : 0);
            }
        }
        return result;
    }
}
