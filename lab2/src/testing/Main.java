package testing;

import xyz.sky731.programming.lab3.*;

public class Main {
    public static void main(String[] args) {
        Human[] emplForBred1 = {new Human(new Home(10), 0, "Empl1"),
                                new Human(new Home(10), 0, "Empl2")};
        Human hostessBred1 = new Human(new Home(15), 0, "bred1empl");
        Factory bred1 = new Factory(emplForBred1, hostessBred1);

        Human[] emplForBred2 = {new Human(new Home(10), 0, "Empl1"),
                                new Human(new Home(10), 0, "Empl2")};
        Human hostessBred2 = new Human(new Home(15), 0, "bred2empl");
        Factory bred2 = new Factory(emplForBred2, hostessBred2);

        Bredlam bredlam = new Bredlam(bred1, bred2);
        System.out.println(bredlam);

        Human hostess = new Human(new Home(1000), 100, "Hostess");
        Human[] employers = {new Human(new Home(10), 0, "Empl1"),
                             new Human(new Home(10), 0, "Empl2")};
        Factory factory = new Factory(employers, hostess);
        hostess.buyBuilding(factory);
        hostess.giveMoney(factory, 50);
        factory.addDepartment(new Department(30, factory));
        factory.addDepartment(new Department(25, factory));
        factory.addDepartment(new Department(13, factory));
        factory.addDepartment(new Department(18, factory));
        StatusOfDepartment status = StatusOfDepartment.WORKING;
        for (int i = 0; i < 100; i++) {
            status = factory.work();
            if (status == StatusOfDepartment.WORKING) {
                factory.decCookedSaltCost();
                System.out.println(factory);
            }
            else break;
        }
        if (status == StatusOfDepartment.WORKING) {
            System.out.println(factory + " не разорился");
        } else {
            System.out.println(factory + " разорился");
        }
    }

}