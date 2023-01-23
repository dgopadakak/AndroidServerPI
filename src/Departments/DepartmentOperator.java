package Departments;

import java.util.ArrayList;
import java.util.Objects;

public class DepartmentOperator
{
    private final int id = 1;
    private ArrayList<Department> departments = new ArrayList<>();

    public void addCourier(String groupName, Courier courier)
    {
        boolean isNewGroupNeeded = true;
        for (Department department : departments)
        {
            if (Objects.equals(department.name, groupName))
            {
                isNewGroupNeeded = false;
                department.listOfCouriers.add(courier);
                break;
            }
        }
        if (isNewGroupNeeded)
        {
            ArrayList<Courier> tempArrayList = new ArrayList<>();
            tempArrayList.add(courier);
            departments.add(new Department(groupName, tempArrayList));
        }
    }

    public void delCourier(int groupId, int examId)
    {
        departments.get(groupId).listOfCouriers.remove(examId);
    }

    public void editCourier(int groupId, int examId, Courier newCourier)
    {
        departments.get(groupId).listOfCouriers.set(examId, newCourier);
    }

    public ArrayList<Department> getDepartments()
    {
        return departments;
    }

    public void setDepartments(ArrayList<Department> travelCompanies)
    {
        this.departments = travelCompanies;
    }
}
