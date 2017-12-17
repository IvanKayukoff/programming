package xyz.sky731.programming.lab3;

import java.util.Objects;
import java.util.Random;

public class Department extends Building implements Workable {
    private Factory factory = null;
    protected int money = 0;
    protected Crystals crystals = new Crystals();
    protected CookedSalt cookedSalt = new CookedSalt();

    public Factory getFactory() {
        return factory;
    }

    protected int cookedSaltCost = 0;

    public Department(int cost, Factory factory) {
        super(cost);
        this.factory = factory;
    }

    protected void setCookedSaltCost(int cost) {
        cookedSaltCost = cost;
    }

    protected int getCookedSaltCost() {
        return cookedSaltCost;
    }

    @Override
    public StatusOfDepartment work() {
        if (money < Market.getCrystalCost()) {
            money += factory.giveMoney(Market.getCrystalCost() - money);
        }
        if (buyCrystal()) {
            crystals.useCrystal();
            cookedSalt.addSalt();
            int profit = cookedSalt.sell(cookedSaltCost);
            if (profit == 0) {
                cookedSaltCost = Market.getAvgSaltCost();
                money += cookedSalt.sell(cookedSaltCost);
            } else {
                money += profit;
            }
            money += cookedSalt.sell(cookedSaltCost);
            factory.getMoney(money);
            money = 0;
            return StatusOfDepartment.WORKING;
        } else {
            return StatusOfDepartment.RUINED;
        }
    }

    private boolean buyCrystal() {
        if (money >= Market.getCrystalCost()) {
            crystals.addCrystal();
            money -= Market.getCrystalCost();
            return true;
        } else return false;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Department)) return false;

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
        int result = super.hashCode();
        result = 31 * result + (factory != null ? factory.hashCode() : 0);
        result = 31 * result + money;
        result = 31 * result + (crystals != null ? crystals.hashCode() : 0);
        result = 31 * result + (cookedSalt != null ? cookedSalt.hashCode() : 0);
        result = 31 * result + cookedSaltCost;
        return result;
    }
}
