package ru.ipim.phonebook.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ipim.phonebook.repository.EmployeeRepository;
import ru.ipim.phonebook.repository.JobRepository;

@RestController
public class ReportController {
    private final EmployeeRepository employeeRepository;
    private final JobRepository jobRepository;

    ReportController(EmployeeRepository employeeRepository, JobRepository jobRepository) {
        this.employeeRepository = employeeRepository;
        this.jobRepository = jobRepository;
    }

    @GetMapping("/reports/employee/count-all")
    public long countAllEmployees() {
        return employeeRepository.count();
    }

    @GetMapping("/reports/job/count-all")
    public long countAllJobs() {
        return jobRepository.count();
    }

    @GetMapping("/reports/employee/count-month")
    public long countEmployeesForMonth() {
        return employeeRepository.countForMonth();
    }
}