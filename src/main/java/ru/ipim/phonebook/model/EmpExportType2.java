package ru.ipim.phonebook.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.style.ToStringCreator;

import java.util.Date;

//Response Model
    @Data
    @AllArgsConstructor
    @Getter
    @Setter
    public class EmpExportType2 {
    private String firstName;
    private String lastName;
    private String mobilePhone;
    private String email;
    private String company;
    private String jobTitle;
    private String address;

        @Override
        public String toString() {
            return new ToStringCreator(this)
                    .append("firstName", this.getFirstName())
                    .append("lastName", this.getLastName())
                    .append("mobilePhone", this.getMobilePhone())
                    .append("email", this.getEmail())
                    .append("company", this.getCompany())
                    .append("jobTitle", this.getJobTitle())
                    .append("address", this.getAddress())
                    .toString();
        }
    }


