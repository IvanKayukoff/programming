package xyz.sky731.programming.lab5;

import xyz.sky731.programming.lab3.Bredlam;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class Bredlams {
    @XmlElement(name = "bredlams")
    private List<Bredlam> bredlams;

    public List<Bredlam> getList() {
        return bredlams;
    }

    public void setList(List<Bredlam> list) {
        this.bredlams = list;
    }
}
