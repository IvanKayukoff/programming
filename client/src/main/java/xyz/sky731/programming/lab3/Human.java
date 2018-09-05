package xyz.sky731.programming.lab3;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Human")
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY)
public class Human implements Ownable, Serializable {

    private int money = 1000;

    @XmlElement(name = "building", type = Home.class)
    private List<Building> buildings;

    private String name;

    @XmlElement(name = "number")
    private static int number = 0;

    public Human(String name, int money) {
        this.money = money;
        buildings = new ArrayList<>();
        buildings.add(new Home());
        this.name = name;
        number++;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public Human() {
        buildings = new ArrayList<>();
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
    public void sellBuilding(Building building) throws NoExistException {
        if (building == null) return;
        money += building.getCost();
        if (!buildings.remove(building)) {
            throw new NoExistException(getName() + " не владеет этим! ", building);
        }
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
        return (name != null ? name : "Anonymous");
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

class NoExistException extends Exception {
    public NoExistException(String message, Building building) {
        super(building.toString() + " " + message);
    }
}
