package xyz.sky731.programming.lab3;

public class Home extends Building {
    private static final int COST = 100;
    public Home() {
        super(COST);
    }

    @Override
    public String toString() {
        return "Дом";
    }
}
