package xyz.sky731.programming.lab3;

import java.util.ArrayList;
import java.util.List;

public class Factory extends Building {
    private List<Department> departments = null;

    public Factory(String name) {
        super(0);
        departments = new ArrayList<Department>();
    }

    public void addDepartment(Department department) {
        departments.add(department);
    }
}
