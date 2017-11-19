package xyz.sky731.programming.lab3;

import java.util.ArrayList;
import java.util.List;

public class Bredlam {
    private List<Factory> factories = null;

    public Bredlam(Factory... factories) {
        this.factories = new ArrayList<>();
        for (Factory elem: factories) {
            this.factories.add(elem);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bredlam bredlam = (Bredlam) o;

        if (factories != null ? !factories.equals(bredlam.factories) : bredlam.factories != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Бредлам из " + factories.size() + " заводов";
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
