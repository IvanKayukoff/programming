package testing;

import xyz.sky731.programming.lab3.*;

public class Main {
    public static void main(String[] args) {
        Human[] empls = new Human[12];
        for (int i = 0; i < empls.length; i++) {
            empls[i] = new Human();
        }

        Factory[] factories = new Factory[3];
        Human[] hostesses = new Human[3];
        for (int i = 0; i < factories.length; i++) {
            hostesses[i] = new Human("hostess" + i, 100000);
            Human[] hs = new Human[3];
            for (int j = 0; j < hs.length; j++) {
                hs[j] = empls[i*4 + j];
            }
            factories[i] = new Factory(hs, hostesses[i]);
            Department dep1 = new Department(100, factories[i]);
            Department dep2 = new Department(101, factories[i]);
            factories[i].addDepartment(dep1);
            factories[i].addDepartment(dep2);
            hostesses[i].giveMoney(factories[i], 10000);
        }

        Bredlam bredlam = new Bredlam(hostesses);
        bredlam.setPredsedatel(new Human("Господин дракула", 10000));

        Human ponchik = new Human("Пончик", 200);
        Human empl1 = new Human();
        Human empl2 = new Human();
        Factory ponchikFac = new Factory(new Human[]{empl1, empl2}, ponchik);
        ponchikFac.addDepartment(new Department(100, ponchikFac));
        ponchik.giveMoney(ponchikFac, 200);

        Human otherHostess = new Human("Мелкий хозяйчик", 100);
        Human empl3 = new Human();
        Human empl4 = new Human();
        Factory otherFac = new Factory(new Human[]{empl3, empl4}, otherHostess);
        otherFac.addDepartment(new Department(101, otherFac));
        otherHostess.giveMoney(otherFac, 100);

        boolean isOutOfMoney = true;
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < factories.length; j++) {
                factories[j].work();
                factories[j].decCookedSaltCost();
            }
            if (ponchikFac.work() == StatusOfDepartment.WORKING) {
                System.out.println(ponchikFac);
                if (ponchikFac.getMoney() < Market.getCrystalCost()) {
                    ponchik.sellBuilding(ponchik.getHome());
                    ponchik.giveMoney(ponchikFac, ponchik.getMoney());
                }
            } else {
                System.out.println(ponchik.getName() + " разорился;(");
                break;
            }
            if (otherFac.work() == StatusOfDepartment.WORKING) {
                System.out.println(otherFac);
            } else if (isOutOfMoney) {
                System.out.println(otherHostess.getName() + " разорился;(");
                isOutOfMoney = !isOutOfMoney;
            }
        }

    }

}

