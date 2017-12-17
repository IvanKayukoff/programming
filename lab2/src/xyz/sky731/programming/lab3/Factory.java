package xyz.sky731.programming.lab3;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Factory extends Building implements Workable {
    private static int SALARY = 3;

    private int money = 0;
    private List<Human> employers = null;
    private List<Department> departments;
    private Human hostess = null;

    public Factory(Human[] employers, Human hostess) {
        super(0);
        this.employers = new ArrayList<Human>();
        this.departments = new ArrayList<Department>();
        for (Human human: employers) {
            this.employers.add(human);
        }
        this.hostess = hostess;
        hostess.addBuildings(this);
    }

    public boolean isBig() {
        return departments.size() > 2 ? true : false;
    }

    public void addDepartment(Department department) {
        if (department == null) return;
        departments.add(department);
        departments.add(department);
    }

    @Override
    public StatusOfDepartment work() {
        if (departments.size() == 0) return StatusOfDepartment.RUINED;
        for (int i = 0; i < departments.size(); i++) {
            if (departments.get(i).work() == StatusOfDepartment.RUINED) {
                departments.remove(i);
                System.out.println(hostess.getName() + " продал помещение для завода");
                if (departments.size() == 0) {
                    return StatusOfDepartment.RUINED;
                } else return StatusOfDepartment.WORKING;
            } else {
                giveSalary(departments.get(i));
            }
        }
        return StatusOfDepartment.WORKING;
    }

    public void decCookedSaltCost() {
        for (Department department: departments) {
            if (department.getCookedSaltCost() > 0) {
                department.setCookedSaltCost(department.getCookedSaltCost() - 1);
            }
        }
    }

    public int getMoney() {
        return money;
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
        departments.remove(department);
    }

    protected void addMoney(int money) {
        this.money += money;
    }

    @Override
    public String toString() {
        return "Завод " + hostess.getName() + " с " + departments.size() + " помещениями, имеющий "
                + money + " сантиков";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Factory)) return false;

        Factory factory = (Factory) o;

        if (money != factory.money) return false;
        if (employers != null ? !employers.equals(factory.employers) :
                factory.employers != null) return false;
        if (departments != null ? !departments.equals(factory.departments) :
                factory.departments != null)
            return false;
        if (hostess != null ? !hostess.equals(factory.hostess) :
                factory.hostess != null) return false;

        return true;
    }

    public Human getHostess() {
        return hostess;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + money;
        result = 31 * result + (employers != null ? employers.hashCode() : 0);
        result = 31 * result + (departments != null ? departments.hashCode() : 0);
        result = 31 * result + (hostess != null ? hostess.hashCode() : 0);
        if (employers != null && employers.size() > 0) {
            for (Human emp : employers) {
                result = 31 * result + (emp != null ? emp.hashCode() : 0);
            }
        }
        if (departments != null && departments.size() > 0) {
            for (Department dep : departments) {
                result = 31 * result + (dep != null ? dep.hashCode() : 0);
            }
        }
        return result;

    }
}
