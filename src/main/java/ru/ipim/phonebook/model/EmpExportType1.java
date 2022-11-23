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
public class EmpExportType1 {
    private String firstName;
    private String lastName;
    private Date birthdate;
    private String mobilePhone;
    private String workPhone;
    private String email;

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("firstName", this.getFirstName())
                .append("lastName", this.getLastName())
                .append("birthdate", this.getBirthdate().toString())
                .append("mobilePhone", this.getMobilePhone())
                .append("workPhone", this.getWorkPhone())
                .append("email", this.getEmail())
                .toString();
    }
}


