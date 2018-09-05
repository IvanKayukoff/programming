package xyz.sky731.programming.lab3;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlTransient
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY)
public abstract class Building implements Serializable {

    @XmlElement(name = "cost")
    private int cost = 0;

    public Building(int cost) {
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o != null && o instanceof Building) {
            Building building = (Building) o;
            if (cost != building.cost) return false;
            return true;
        } else return false;
    }

    @Override
    public String toString() {
        return "Недвижимость стоимостью " + cost + " сантиков";
    }

    @Override
    public int hashCode() {
        return cost;
    }
}
