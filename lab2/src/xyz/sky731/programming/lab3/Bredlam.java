package xyz.sky731.programming.lab3;

import java.util.ArrayList;
import java.util.List;

public class Bredlam {
    private List<Human> humans = null;
    private Human predsedatel = null;

    public Bredlam(Human... humans) {
        this.humans = new ArrayList<>();
        for (Human human: humans) {
            if (human.isBig()) {
                this.humans.add(human);
            }
        }
        if (this.humans.size() == 0) {
            System.out.println("Боженька создал пустой бредлам");
        } else if (this.humans.size() == 1) {
            System.out.println("Один человек зарегистрировал себя как бредлам");
        } else {
            System.out.println("Владельцы крупных заводов объединились в бредлам");
        }
    }

    public void setPredsedatel(Human human) {
        if (human == null) return;
        humans.add(human);
        predsedatel = human;
        System.out.println("Председателем выбрали: " + human.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Bredlam)) return false;
        Bredlam bredlam = (Bredlam) o;
        if (humans != null ? !humans.equals(bredlam.humans) :
                bredlam.humans != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Бредлам из " + humans.size() + " людей";
    }

    @Override
    public int hashCode() {
        int result = humans != null ? humans.hashCode() : 0;
        result = 31 * result + (predsedatel != null ? predsedatel.hashCode() : 0);
        if (humans != null || humans.size() > 0) {
            for (Human human : humans) {
                result = 31 * result + (human != null ? human.hashCode() : 0);
            }
        }
        return result;
    }
}
