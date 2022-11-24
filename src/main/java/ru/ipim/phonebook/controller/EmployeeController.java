package ru.ipim.phonebook.controller;

import org.springframework.web.bind.annotation.*;

import ru.ipim.phonebook.dto.EmployeeDto;
import ru.ipim.phonebook.repository.EmployeeRepository;
import ru.ipim.phonebook.model.Employee;
import ru.ipim.phonebook.repository.JobRepository;

import java.util.List;
import java.util.Optional;

@RestController
public class EmployeeController {
    private final EmployeeRepository employeeRepository;
    private final JobRepository jobRepository;

    EmployeeController(EmployeeRepository employeeRepository, JobRepository jobRepository) {
        this.employeeRepository = employeeRepository;
        this.jobRepository = jobRepository;
    }

    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @GetMapping("/employees/{employeeId}")
    public Optional<Employee> getEmployee(@PathVariable("employeeId") Long employeeId) {
        return employeeRepository.findById(employeeId);
    }

    @PostMapping("/employees")
    Employee createEmployee(@RequestBody EmployeeDto newEmployeeDto) {
        Employee newEmployee = new Employee(
                newEmployeeDto.getFirstName(),
                newEmployeeDto.getLastName(),
                newEmployeeDto.getBirthdate(),
                newEmployeeDto.getMobilePhone(),
                newEmployeeDto.getWorkPhone(),
                newEmployeeDto.getEmail()
        );
        Long jobId = newEmployeeDto.getJobId();
        if (jobId != null) {
            jobRepository.findById(jobId).ifPresentOrElse(newEmployee::setJob,
                    () -> System.out.println("Место работы не найдено"));
        }
        return employeeRepository.save(newEmployee);
    }

    @GetMapping("/employees/{employeeId}/coworkers-company")
    public List<Employee> getCompanyCoworkers(@PathVariable("employeeId") Long employeeId) {
        return employeeRepository.getCompanyCoworkers(employeeId);
    }
}