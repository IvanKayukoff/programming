package xyz.sky731.programming.lab3;

public class Market {
    private static int avgSaltCost = 30;
    private static int crystalCost = 5;

    static boolean buySalt(int cost) {
        if (cost <= avgSaltCost && cost > 0) {
            if (cost < avgSaltCost) {
                System.out.println("Соль дешевеет..");
            }
            avgSaltCost = (avgSaltCost + cost) / 2;
            return true;
        } else return false;
    }

    public static int getCrystalCost() {
        return crystalCost;
    }

    static int getAvgSaltCost() {
        return avgSaltCost;
    }

    @Override
    public String toString() {
        return "Рынок, соль стоит: " + Market.avgSaltCost;
    }
}
