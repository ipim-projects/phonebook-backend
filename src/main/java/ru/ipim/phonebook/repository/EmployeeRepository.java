package ru.ipim.phonebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.ipim.phonebook.model.Employee;
import ru.ipim.phonebook.projection.EmployeeInfo;

@RepositoryRestResource(excerptProjection = EmployeeInfo.class)
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
