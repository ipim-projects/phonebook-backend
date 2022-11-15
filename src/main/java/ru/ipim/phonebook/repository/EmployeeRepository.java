package ru.ipim.phonebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ipim.phonebook.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
