package ru.ipim.phonebook.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class Job implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String company;

    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @Column
    private String address;
}
