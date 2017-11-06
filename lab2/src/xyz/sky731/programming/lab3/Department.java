package xyz.sky731.programming.lab3;

public class Department extends Building implements Workable {
    String name;

    public Department(String name, int cost) {
        super(cost);
        this.name = name;
    }

    @Override
    public void work() {

    }
}
