package ru.ipim.phonebook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ru.ipim.phonebook.repository.EmployeeRepository;
import ru.ipim.phonebook.model.Employee;

import java.util.List;

@RestController
public class EmployeeController {
    private final EmployeeRepository employeeRepository;

    @Autowired
    EmployeeController(EmployeeRepository repository) {
        this.employeeRepository = repository;
    }

    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @PostMapping("/employees")
    Employee createOrSaveEmployee(@RequestBody Employee newEmployee) {
        return employeeRepository.save(newEmployee);
    }
}