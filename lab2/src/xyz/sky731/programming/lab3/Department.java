package xyz.sky731.programming.lab3;

import java.util.Objects;
import java.util.Random;

public class Department extends Building implements Workable {
    private Factory factory = null;
    private int money = 0;
    private Crystals crystals = new Crystals(5);
    private CookedSalt cookedSalt = new CookedSalt(30);

    public Department(int cost, Factory factory) {
        super(cost);
        this.factory = factory;
    }

    protected void setCookedSaltCost(int cost) {
        cookedSalt.setCost(cost);
    }

    protected int getCookedSaltCost() {
        return cookedSalt.getCost();
    }

    @Override
    public StatusOfDepartment work() {
        if (money < crystals.getCost()) {
            money += factory.giveMoney(crystals.getCost() - money);
        }
        if (money >= crystals.getCost()) {
            crystals.buyCrystal();
            money -= crystals.getCost();
            crystals.useCrystal();
            cookedSalt.cookSalt();
            money += cookedSalt.sell();
            factory.getMoney(money);
            money = 0;
            return StatusOfDepartment.WORKING;
        } else {
            return StatusOfDepartment.RUINED;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Department that = (Department) o;

        if (money != that.money) return false;
        if (factory != null ? !factory.equals(that.factory) : that.factory != null) return false;
        if (crystals != null ? !crystals.equals(that.crystals) : that.crystals != null) return false;
        if (cookedSalt != null ? !cookedSalt.equals(that.cookedSalt) : that.cookedSalt != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Помещение для завода стоимостью в " + getCost() + " сантиков";
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
