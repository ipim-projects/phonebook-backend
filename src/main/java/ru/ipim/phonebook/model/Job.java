package ru.ipim.phonebook.model;

import lombok.*;
import org.springframework.core.style.ToStringCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "job")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Job implements Serializable {

    @Id
    @SequenceGenerator(name = "jobIdSeq", sequenceName = "job_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jobIdSeq")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "company", nullable = false)
    private String company;

    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @Column(name = "address")
    private String address;

    @Override
    public String toString() {
        return new ToStringCreator(this)

                .append("id", this.getId())
                .append("company", this.getCompany())
                .append("jobTitle", this.getJobTitle())
                .append("address", this.getAddress())
                .toString();
    }

}
