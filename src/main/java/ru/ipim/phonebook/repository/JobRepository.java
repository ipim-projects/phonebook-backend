package ru.ipim.phonebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.ipim.phonebook.model.Job;
import ru.ipim.phonebook.model.StatAddress;
import ru.ipim.phonebook.model.StatCompany;
import ru.ipim.phonebook.model.StatJobs;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    // агрегация по компаниям
    @Query(value = "SELECT new ru.ipim.phonebook.model.StatCompany( c.company AS company, COUNT(c.company) AS cnt) FROM Job AS c GROUP BY c.company ORDER BY cnt DESC, company")
    List<StatCompany> findGroupByCompanyWithJPQL();
    // агрегация по должности
    //@Query(value = "SELECT new ru.ipim.phonebook.model.StatJobs( j.job_title AS jobTitle, COUNT(j.job_title) AS cnt) FROM Job AS j GROUP BY j.job_title ORDER BY cnt DESC, jobTitle")
    //List<StatJobs> findGroupByJobsWithJPQL();
    // агрегация по адресу компании
    @Query(value = "SELECT new ru.ipim.phonebook.model.StatAddress( j.address AS address, COUNT(j.address) AS cnt) FROM Job AS j GROUP BY address ORDER BY cnt DESC, address")
    List<StatAddress> findGroupByAddressWithJPQL();

}
