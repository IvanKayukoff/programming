package xyz.sky731.programming.lab3;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.sun.istack.internal.NotNull;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@XmlRootElement(name = "Bredlam")
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY)
public class Bredlam implements Comparable<Bredlam>, Serializable {

    public static class BredlamNameComp implements Comparator<Bredlam> {
        public int compare(Bredlam a, Bredlam b) {
            if (b == null || a == null) return 0;
            if (a.name == null || b.name == null) return 0;
            return a.name.compareTo(b.name);
        }
    }

    @Override
    public int compareTo(Bredlam o) {
        return this.size() - o.size();
    }

    @XmlElement(name = "name")
    String name = null;

    @XmlElement(name = "human", type = Human.class)
    private List<Human> humans = new ArrayList<>();

    @XmlElement(name = "predsedatel", type = Human.class)
    private Human predsedatel = null;

    @XmlElement(name = "endOfLight")
    private boolean endOfLight = false;

    private Bredlam() {}

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
        if (o == null || getClass() != o.getClass()) return false;
        Bredlam bredlam = (Bredlam) o;
        return endOfLight == bredlam.endOfLight &&
                Objects.equals(name, bredlam.name) &&
                Objects.equals(humans, bredlam.humans) &&
                Objects.equals(predsedatel, bredlam.predsedatel);
    }

    @Override
    public String toString() {
        return name + " bredlam with " + humans.size() + " humans";
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
        try {
            return humans.size() + (predsedatel != null ? 1 : 0);
        } catch (NullPointerException ex) {
            return 0;
        }
    }
}

class NoPredsFoundException extends RuntimeException {
    public NoPredsFoundException(String message) {
        super(message);
    }
}
