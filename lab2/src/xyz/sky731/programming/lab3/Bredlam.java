package xyz.sky731.programming.lab3;

import com.sun.istack.internal.NotNull;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Bredlam")
public class Bredlam {

    @XmlElement(name = "name")
    String name = null;

    @XmlElement(name = "human", type = Human.class)
    private List<Human> humans = null;

    @XmlElement(name = "predsedatel", type = Human.class)
    private Human predsedatel = null;

    @XmlElement(name = "endOfLight")
    private boolean endOfLight = false;

    public Bredlam() {}

    public Bredlam(String name, Human... humans) {
        this.humans = new ArrayList<>();
        this.name = name;
        for (Human human: humans) {
            this.humans.add(human);
        }
        if (this.humans.size() == 0) {
            System.out.println("Боженька создал пустой бредлам");
        } else if (this.humans.size() == 1) {
            System.out.println("Один человек зарегистрировал себя как бредлам");
        } else {
            System.out.println("Владельцы крупных заводов объединились в бредлам");
        }
    }

    public void zasedanie() {
        class ZacedException extends RuntimeException {
            ZacedException(String message) {
                super(message);
            }
        }
        if (humans.size() == 0) {
            throw new ZacedException("Бредлам слишком пуст, чтобы заседать что-либо");
        }
        if (Math.random() > 0.5) {
            endOfLight = true;
            System.out.println("На заседании решили разделаться" +
                    " с владельцами мелких заводов");
        } else {
            endOfLight = false;
            System.out.println("На заседании решили, что все и так хорошо");
        }
    }

    public void choosePredsedatel() {
        Human res = null;
        for (Human h : humans) {
            if (h == null) continue;
            if (res == null) res = h;
            if (h.getMoney() > res.getMoney()) {
                res = h;
            }
        }
        if (res == null) {
            throw new NoPredsFoundException("Бредлам слишком пуст..");
        }
        predsedatel = res;
        System.out.println("Председателем " + name + " бредлама выбрали: " +
                predsedatel.getName());
    }

    public boolean getEndOfLight() {
        return endOfLight;
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
        return name + " бредлам из " + humans.size() + " людей";
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

    public int size() {
        return humans.size() + (predsedatel != null ? 1 : 0);
    }
}

class NoPredsFoundException extends RuntimeException {
    public NoPredsFoundException(String message) {
        super(message);
    }
}
