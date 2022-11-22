package ru.ipim.phonebook.controller;

import org.springframework.web.bind.annotation.*;

import ru.ipim.phonebook.model.Job;
import ru.ipim.phonebook.repository.EmployeeRepository;
import ru.ipim.phonebook.model.Employee;
import ru.ipim.phonebook.repository.JobRepository;

import java.util.List;

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

    @PostMapping("/employees")
    Employee createOrSaveEmployee(@RequestBody Employee newEmployee) {
        Job job = newEmployee.getJob();
        if (job != null && job.getId() != null) {
            job = jobRepository.findById(job.getId()).get();
            newEmployee.setJob(job);
        }
        return employeeRepository.save(newEmployee);
    }

    @GetMapping("/employees/{employeeId}/coworkers-company")
    public List<Employee> getCompanyCoworkers(@PathVariable("employeeId") Long employeeId) {
        return employeeRepository.getCompanyCoworkers(employeeId);
    }
}