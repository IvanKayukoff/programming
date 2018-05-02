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
            Place.Coast coast1 = new Place.Coast(200, new Crystals());
            DepartmentEx dep1 = new DepartmentEx(100, factories[i], coast1);
            dep1.upgrade();
            Place.Coast coast2 = new Place.Coast(202, new Crystals());
            DepartmentEx dep2 = new DepartmentEx(101, factories[i], coast2);
            dep2.upgrade();
            factories[i].addDepartment(dep1);
            factories[i].addDepartment(dep2);
            hostesses[i].giveMoney(factories[i], 10000);
        }

        Bredlam bredlam = new Bredlam("Соляной", hostesses);
        bredlam.choosePredsedatel();
        bredlam.zasedanie();

        Human ponchik = new Human("Пончик", 200);
        Human empl1 = new Human();
        Human empl2 = new Human();

        abstract class EndOfMoney {
            public void broke(Human ponkik) {
                System.out.println(ponkik.getName() + " разорился;(");
            }
        }

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
                if (bredlam.getEndOfLight()) factories[j].decCookedSaltCost();
            }
            EndOfMoney endOfMoney = new EndOfMoney() {
                @Override
                public void broke(Human ponkik) {
                    super.broke(ponkik);
                }
            };
            if (ponchikFac.work() == StatusOfDepartment.WORKING) {
                System.out.println(ponchikFac);
                if (ponchikFac.getMoney() < Market.getCrystalCost()) {
                    try {
                        ponchik.sellBuilding(ponchik.getHome());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        System.out.println(ponchik.getName() + " не может продать то, чего у него нету");
                    }
                    ponchik.giveMoney(ponchikFac, ponchik.getMoney());
                }
            } else {
                endOfMoney.broke(ponchik);
                break;
            }
            if (otherFac.work() == StatusOfDepartment.WORKING) {
                System.out.println(otherFac);
            } else if (isOutOfMoney) {
                endOfMoney.broke(otherHostess);
                isOutOfMoney = !isOutOfMoney;
            }
        }

    }

}

