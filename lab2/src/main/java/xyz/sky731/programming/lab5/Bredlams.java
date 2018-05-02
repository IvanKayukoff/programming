package xyz.sky731.programming.lab5;

import xyz.sky731.programming.lab3.Bredlam;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class Bredlams {
    private List<Bredlam> bredlam;

    public List<Bredlam> getBredlam() {
        return bredlam;
    }

    public void setBredlam(List<Bredlam> list) {
        this.bredlam = list;
    }
}
