package Departments;

import java.util.ArrayList;

public class Department
{
    String name;
    ArrayList<Courier> listOfCouriers;

    public Department(String name, ArrayList<Courier> listOfCouriers)
    {
        this.name = name;
        this.listOfCouriers = listOfCouriers;
    }
}
