package ru.ipim.phonebook.projection;

import java.util.Date;

/**
 * A Projection for the {@link ru.ipim.phonebook.model.Employee} entity
 */
public interface EmployeeInfo {
    Long getId();
    String getFirstName();
    String getLastName();
    Date getBirthdate();
    String getMobilePhone();
    String getWorkPhone();
    String getEmail();
    JobInfo getJob();
}