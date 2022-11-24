package ru.ipim.phonebook.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import ru.ipim.phonebook.dto.EmployeeDto;
import ru.ipim.phonebook.exception.ResourceNotFoundException;
import ru.ipim.phonebook.repository.EmployeeRepository;
import ru.ipim.phonebook.model.Employee;
import ru.ipim.phonebook.repository.JobRepository;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@Api("Контроллер работы с таблицей 'Справочник место работы'")
public class EmployeeController {
    private final EmployeeRepository employeeRepository;
    private final JobRepository jobRepository;

    EmployeeController(EmployeeRepository employeeRepository, JobRepository jobRepository) {
        this.employeeRepository = employeeRepository;
        this.jobRepository = jobRepository;
    }

    @GetMapping("/employees")
    @ApiOperation("Получение списка всех записей")
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @GetMapping("/employees/{employeeId}")
    @ApiOperation("Получение одиночной записи по employeeId")
    public Optional<Employee> getEmployee(@PathVariable("employeeId") Long employeeId) {
        return employeeRepository.findById(employeeId);
    }

    @PostMapping("/employees")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Создание новой записи")
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Обновление существующей записи")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("employeeId") Long employeeId, @RequestBody EmployeeDto employeeDto) {
        log.info("Вызов Update Employee employeeId = {}", employeeId);
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Удаление записи")
    public ResponseEntity<String> deleteEmployee(@PathVariable("employeeId") Long employeeId) {
        log.info("Вызов удаление из таблицы Employee для employeeId = {}", employeeId);
        employeeRepository.deleteById(employeeId);
        return ResponseEntity.ok("Employee " + employeeId + " removed successful");
    }

    @GetMapping("/employees/{employeeId}/coworkers-company")
    @ApiOperation("Поиск коллег по входному параметру (employeeId): сотрудники одной и той же с ним организации")
    public List<Employee> getCompanyCoworkers(@PathVariable("employeeId") Long employeeId) {
        log.info("Вызов coworkers-company для employeeId = {}", employeeId);
        return employeeRepository.getCompanyCoworkers(employeeId);
    }

    @GetMapping("/employees/{employeeId}/coworkers-job")
    @ApiOperation("Поиск коллег по входному параметру (employeeId): сотрудники из разных организаций, но с такой же должностью")
    public List<Employee> getJobTitleCoworkers(@PathVariable("employeeId") Long employeeId) {
        log.info("Вызов coworkers-job для employeeId = {}", employeeId);
        return employeeRepository.getJobTitleCoworkers(employeeId);
    }

    @GetMapping("/employees/{employeeId}/coworkers-address")
    @ApiOperation("Поиск коллег по входному параметру (employeeId): сотрудники из разных организаций, но работающих с ним по одному и тому же адресу")
    public List<Employee> getCompanyAddressCoworkers(@PathVariable("employeeId") Long employeeId) {
        log.info("Вызов coworkers-address для employeeId = {}", employeeId);
        return employeeRepository.getCompanyAddressCoworkers(employeeId);
    }
}