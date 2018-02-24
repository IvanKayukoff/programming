package xyz.sky731.programming.lab5;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class JAXBUser<T> {
    private String filename;

    public JAXBUser(String filename) {
        this.filename = filename;
    }

    public String marshal(T data) {
        try {
            File file = new File(filename);
            JAXBContext jaxbContext = JAXBContext.newInstance(data.getClass());
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(data, file);
            jaxbMarshaller.marshal(data, System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    public T unmarshal(T data) {

        T result = null;
        try {
            File file = new File(filename);
            JAXBContext jaxbContext = JAXBContext.newInstance(data.getClass());

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            result = (T) jaxbUnmarshaller.unmarshal(file);
            System.out.println(result);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return result;
    }
}
