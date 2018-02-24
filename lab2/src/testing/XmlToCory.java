package testing;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "XmlToCory")
public class XmlToCory {
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "lastName")
    private String lastName;
    @XmlElement(name = "age")
    private int age;

    public XmlToCory() {
        name = "Vasya";
        lastName = "Sasha";
        age = 42;
    }

    @Override
    public String toString() {
        return "I'm " + name + " " + lastName + ". My age: " + age;
    }
}
