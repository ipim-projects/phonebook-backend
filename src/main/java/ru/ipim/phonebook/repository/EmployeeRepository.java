package ru.ipim.phonebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.ipim.phonebook.model.Employee;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT COUNT(e) FROM Employee e WHERE e.createdAt >= CURRENT_DATE - 30")
    long countForMonth();

    @Query("SELECT e FROM Employee e WHERE e.job.company = (SELECT e1.job.company FROM Employee e1 WHERE e1.id = ?1)")
    List<Employee> getCompanyCoworkers(Long employeeId);
}
