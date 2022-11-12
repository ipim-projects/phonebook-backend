package ru.ipim.phonebook.projection;

/**
 * A Projection for the {@link ru.ipim.phonebook.model.Job} entity
 */
public interface JobInfo {
    Long getId();
    String getCompany();
    String getJobTitle();
    String getAddress();
}