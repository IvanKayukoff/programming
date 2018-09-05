package xyz.sky731.programming.lab3;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY)
public class Home extends Building {

    @XmlElement(name = "staticcost")
    private static final int COST = 100;

    @XmlElement
    private static int homeCounts = 0;

    @XmlElement
    private int id = 0;

    public Home() {
        super(COST);
        id = homeCounts++;
    }

    @Override
    public String toString() {
        return "Дом";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Home home = (Home) o;

        if (id != home.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id;
        return result;
    }
}
