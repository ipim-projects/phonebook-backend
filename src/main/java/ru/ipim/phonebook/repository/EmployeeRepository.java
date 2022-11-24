package ru.ipim.phonebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.ipim.phonebook.model.Employee;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT COUNT(e) FROM Employee e WHERE e.createdAt >= CURRENT_DATE - 30")
    long countForMonth();

    @Query("SELECT e FROM Employee e WHERE e.id <> ?1" +
            "AND e.job.company = (SELECT e1.job.company FROM Employee e1 WHERE e1.id = ?1)")
    List<Employee> getCompanyCoworkers(Long employeeId);

    @Query("SELECT e FROM Employee e WHERE e.id <> ?1" +
            "AND e.job.jobTitle = (SELECT e1.job.jobTitle FROM Employee e1 WHERE e1.id = ?1)")
    List<Employee> getJobTitleCoworkers(Long employeeId);

    @Query("SELECT e FROM Employee e WHERE e.id <> ?1" +
            "AND e.job.address = (SELECT e1.job.address FROM Employee e1 WHERE e1.id = ?1)")
    List<Employee> getCompanyAddressCoworkers(Long employeeId);
}
