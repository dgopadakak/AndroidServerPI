package Departments;

public class Courier
{
    String name;
    String transport;
    int numOfNotDone;
    String dateOfBirth;
    String dateStart;
    int numOfDone;
    int isOutsideTheCity;
    String comment;

    public Courier(String name, String transport, int numOfNotDone, String dateOfBirth, String dateStart, int numOfDone,
                   int isOutsideTheCity, String comment)
    {
        this.name = name;
        this.transport = transport;
        this.numOfNotDone = numOfNotDone;
        this.dateOfBirth = dateOfBirth;
        this.dateStart = dateStart;
        this.numOfDone = numOfDone;
        this.isOutsideTheCity = isOutsideTheCity;
        this.comment = comment;
    }
}
