package ru.ipim.phonebook.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.ipim.phonebook.dto.EmployeeDto;
import ru.ipim.phonebook.exception.ResourceNotFoundException;
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
    public ResponseEntity<Employee> createEmployee(@RequestBody EmployeeDto newEmployeeDto) {
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
                    () -> {
                        throw new ResourceNotFoundException("Job " + jobId + " not found");
                    });
        }
        return ResponseEntity.ok().body(employeeRepository.save(newEmployee));
    }

    @PutMapping("/employees/{employeeId}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("employeeId") Long employeeId, @RequestBody EmployeeDto employeeDto) {
        final Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        final Employee employee = employeeOptional.orElseThrow(() -> new ResourceNotFoundException("Сотрудник с идентификатором = {" + employeeId + "} не найден"));

        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setWorkPhone(employeeDto.getWorkPhone());
        employee.setMobilePhone(employeeDto.getMobilePhone());
        employee.setEmail(employeeDto.getEmail());
        employee.setBirthdate(employeeDto.getBirthdate());

        Long jobId = employeeDto.getJobId();
        if (jobId != null) {
            jobRepository.findById(jobId).ifPresentOrElse(employee::setJob,
                    () -> {
                        throw new ResourceNotFoundException("Job " + jobId + " not found");
                    });
        }
        return ResponseEntity.ok().body(employeeRepository.save(employee));
    }

    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("employeeId") Long employeeId) {
        employeeRepository.deleteById(employeeId);
        return ResponseEntity.ok("Employee " + employeeId + " removed successful");
    }

    @GetMapping("/employees/{employeeId}/coworkers-company")
    public List<Employee> getCompanyCoworkers(@PathVariable("employeeId") Long employeeId) {
        return employeeRepository.getCompanyCoworkers(employeeId);
    }

    @GetMapping("/employees/{employeeId}/coworkers-job")
    public List<Employee> getJobTitleCoworkers(@PathVariable("employeeId") Long employeeId) {
        return employeeRepository.getJobTitleCoworkers(employeeId);
    }

    @GetMapping("/employees/{employeeId}/coworkers-address")
    public List<Employee> getCompanyAddressCoworkers(@PathVariable("employeeId") Long employeeId) {
        return employeeRepository.getCompanyAddressCoworkers(employeeId);
    }
}