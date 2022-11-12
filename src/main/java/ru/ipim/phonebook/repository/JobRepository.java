package ru.ipim.phonebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.ipim.phonebook.model.Job;
import ru.ipim.phonebook.projection.JobInfo;

@RepositoryRestResource(excerptProjection = JobInfo.class)
public interface JobRepository extends JpaRepository<Job, Long> {
}
