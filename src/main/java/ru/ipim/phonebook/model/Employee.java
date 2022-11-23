package ru.ipim.phonebook.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.core.style.ToStringCreator;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
//import java.time.LocalDate;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "employee")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@DynamicInsert
public class Employee implements Serializable {

    @Id
    @SequenceGenerator(name = "employeeIdSeq", sequenceName = "employee_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employeeIdSeq")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "birthdate", columnDefinition = "DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date birthdate;
    //ig: private LocalDate birthdate;

    @Column(name = "mobile_phone", unique = true, nullable = false)
    private String mobilePhone;

    @Column(name = "work_phone")
    private String workPhone;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    @JsonIgnore
    @ColumnDefault("now()")
    private Date createdAt;
    //ig: private LocalDate createdAt;

    //@ManyToOne(fetch = FetchType.EAGER)
    //@JoinColumn(name = "job_id")
    //private Job job;
    @Column(name = "job_id")
    private Long jobId;

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("id", this.getId())
                .append("firstName", this.getFirstName())
                .append("lastName", this.getLastName())
                .append("workPhone", this.getWorkPhone())
                .append("mobilePhone", this.getMobilePhone())
                .append("email", this.getEmail())
                .append("birthDate", this.getBirthdate())
                .append("jobId", this.getJobId())
                //.append("jobTitle", this.getJob().getJobTitle())
                .toString();
    }

}
