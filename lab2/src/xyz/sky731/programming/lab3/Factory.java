package xyz.sky731.programming.lab3;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Factory extends Building implements Workable {
    private static int SALARY = 3;

    private int money = 0;
    private List<Human> employers = null;
    private Set<Department> departmentsSet;
    private Human hostess = null;

    public Factory(Human[] employers, Human hostess) {
        super(0);
        this.employers = new ArrayList<Human>();
        departmentsSet = Collections.newSetFromMap(new ConcurrentHashMap<>());
        for (Human human: employers) {
            this.employers.add(human);
        }
        this.hostess = hostess;
    }

    public void addDepartment(Department department) {
        if (department == null) return;
        departmentsSet.add(department);
    }

    @Override
    public StatusOfDepartment work() {
        if (departmentsSet.size() == 0) return StatusOfDepartment.RUINED;
        for (Department element: departmentsSet) {
            if (element.work() == StatusOfDepartment.RUINED) {
                sellDepartment(element);
                if (departmentsSet.size() == 0) {
                    return StatusOfDepartment.RUINED;
                }
            } else {
                giveSalary(element);
            }

        }
        return StatusOfDepartment.WORKING;
    }

    public void decCookedSaltCost() {
        for (Department department: departmentsSet) {
            if (department.getCookedSaltCost() > 0) {
                department.setCookedSaltCost(department.getCookedSaltCost() - 1);
            }
        }
    }

    private void giveSalary(Department department) {
        for (Human human: employers) {
            if (money >= SALARY) {
                human.getSalary(SALARY);
                money -= SALARY;
            } else {
                sellDepartment(department);
            }
        }
    }

    protected int giveMoney(int money) {
        if (this.money >= money) {
            this.money -= money;
            return money;
        } else {
            return 0;
        }
    }

    protected void getMoney(int money) {
        this.money += money;
    }

    private void sellDepartment (Department department) {
        money += department.getCost();
        departmentsSet.remove(department);
    }

    protected void addMoney(int money) {
        this.money += money;
    }

    @Override
    public String toString() {
        return "Завод с " + departmentsSet.size() + " помещениями, имеющий "
                + money + " сантиков";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Factory factory = (Factory) o;

        if (money != factory.money) return false;
        if (employers != null ? !employers.equals(factory.employers) : factory.employers != null) return false;
        if (departmentsSet != null ? !departmentsSet.equals(factory.departmentsSet) : factory.departmentsSet != null)
            return false;
        if (hostess != null ? !hostess.equals(factory.hostess) : factory.hostess != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
