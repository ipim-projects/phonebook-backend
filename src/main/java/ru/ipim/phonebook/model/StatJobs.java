package ru.ipim.phonebook.model;

import lombok.*;
import org.springframework.core.style.ToStringCreator;

//Response Model
@Data
@AllArgsConstructor
@Getter
@Setter
public class StatJobs {
    private String jobTitle;
    private Long   cnt;

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("jobTitle", this.getJobTitle())
                .append("cnt", this.getCnt().toString())
                .toString();
    }
}
