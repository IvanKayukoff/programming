package xyz.sky731.programming.lab3;

public class DepartmentEx extends Department {
    private Place.Coast coast = null;
    private int sizeCycle = 1;
    private boolean isModernize = false;
    private static int needToThink = 0;
    public DepartmentEx(int cost, Factory factory, Place.Coast coast) {
        super(cost, factory);
        this.coast = coast;
    }

    public boolean upgrade() {
        if (!isModernize) {
            isModernize = true;
            sizeCycle = 5;
            System.out.println("Начинаем использовать" +
                    " усовершенствованные механизмы");
            return true;
        }
        return false;
    }

    @Override
    public StatusOfDepartment work() {
        if (needToThink > 100) {
            needToThink = 0;
            getFactory().getHostess().think();
        } else {
            needToThink++;
        }
        if (coast.useCrystals(sizeCycle)) {
            cookedSalt.addSalt();
            for (int i = 0; i < sizeCycle; i++) {
                int profit = cookedSalt.sell(cookedSaltCost);
                if (profit == 0) {
                    cookedSaltCost = Market.getAvgSaltCost();
                    money += cookedSalt.sell(cookedSaltCost);
                } else {
                    money += profit;
                }
                money += cookedSalt.sell(cookedSaltCost);
                getFactory().getMoney(money);
                money = 0;
            }
            return StatusOfDepartment.WORKING;
        } else {
            return super.work();
        }
    }
}
