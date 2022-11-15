package ru.ipim.phonebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ipim.phonebook.model.Job;

public interface JobRepository extends JpaRepository<Job, Long> {
}
