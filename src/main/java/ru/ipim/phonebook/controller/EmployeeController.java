package ru.ipim.phonebook.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.minidev.json.JSONObject;
import lombok.extern.slf4j.Slf4j;

import ru.ipim.phonebook.repository.EmployeeRepository;
import ru.ipim.phonebook.repository.JobRepository;

import ru.ipim.phonebook.model.Employee;
import ru.ipim.phonebook.model.EmpExportType1;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.ArrayList;
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

    // Все записи таблицы Employee
    @GetMapping("/employees")
    @ApiOperation("Получение списка всех записей")
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    /*
    TODO: нужен вывод сотрудников вместе с должностями, как было сделано в master (у тебя только jobId):
    {
        "id": 1,
        "firstName": "Илон",
        "lastName": "Маск",
        "birthdate": 46904400000,
        "mobilePhone": "123-111-11-01",
        "workPhone": "495-111-11-01",
        "email": "ilon@cbr.ru",
        "job": {
                  "id": 2,
                  "company": "Amazon",
                  "jobTitle": "Глава Amazon",
                  "address": "AddressAmazon"
               }
    }
     */

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Веб-форма поиска коллег по выбранному абоненту, в зависимости от выбранного флага:
    // 1. Флаг  - "сотрудники одной и той же с ним организации"
    @GetMapping(value = "/employees/{employeeId}/coworkers-company")
    @ApiOperation("Поиск коллег по входному параметру (employeeId): сотрудники одной и той же с ним организации")
    public @ResponseBody List<JSONObject> findCoworkersCompany(@PathVariable("employeeId") @Min(1) Long employeeId) {
        List<EmpExportType1> entityList = employeeRepository.findCoworkersCompanyWithJPQL(employeeId);

        List<JSONObject> entities = new ArrayList<JSONObject>();
        for (EmpExportType1 n : entityList) {
            JSONObject entity = new JSONObject();
            entity.put("lastName", n.getLastName());
            entity.put("firstName", n.getFirstName());
            entity.put("birthdate", n.getBirthdate().toString());
            entity.put("mobilePhone", n.getMobilePhone());
            entity.put("workPhone", n.getWorkPhone());
            entity.put("email", n.getEmail());
            entities.add(entity);
        }
        log.info("Результаты поиска флаг 1: 'коллеги по организации' для сотрудника с id = {}, количество записей: {}", employeeId, entityList.stream().count());
        log.info("Подготовлены следующие данные: {}", entities);
        return entities;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Веб-форма поиска коллег по выбранному абоненту, в зависимости от выбранного флага:
    // 2. Флаг  - "сотрудники из разных организаций, но с такой же должностью"
    @GetMapping(value = "/employees/{employeeId}/coworkers-job")
    @ApiOperation("Поиск коллег по входному параметру (employeeId): сотрудники из разных организаций, но с такой же должностью")
    public @ResponseBody List<JSONObject> findCoworkersJobTitle(@PathVariable("employeeId") @Min(1) Long employeeId) {
        List<EmpExportType1> entityList = employeeRepository.findCoworkersJobTitleWithJPQL(employeeId);

        List<JSONObject> entities = new ArrayList<JSONObject>();
        for (EmpExportType1 n : entityList) {
            JSONObject entity = new JSONObject();
            entity.put("lastName", n.getLastName());
            entity.put("firstName", n.getFirstName());
            entity.put("birthdate", n.getBirthdate().toString());
            entity.put("mobilePhone", n.getMobilePhone());
            entity.put("workPhone", n.getWorkPhone());
            entity.put("email", n.getEmail());
            entities.add(entity);
        }
        log.info("Результаты поиска флаг 2: 'коллеги по должности' для сотрудника с id = {}, количество записей: {}", employeeId, entityList.stream().count());
        log.info("Подготовлены следующие данные: {}", entities);
        return entities;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Веб-форма поиска коллег по выбранному абоненту, в зависимости от выбранного флага:
    // 3. Флаг  - "сотрудники из разных организаций, но работающих с ним по одному и тому же адресу"
    @GetMapping(value = "/employees/{employeeId}/coworkers-address")
    @ApiOperation("Поиск коллег по входному параметру (employeeId): сотрудники из разных организаций, но работающих с ним по одному и тому же адресу")
    public @ResponseBody List<JSONObject> findCoworkersAddress(@PathVariable("employeeId") @Min(1) Long employeeId) {
        List<EmpExportType1> entityList = employeeRepository.findCoworkersAddressWithJPQL(employeeId);

        List<JSONObject> entities = new ArrayList<JSONObject>();
        for (EmpExportType1 n : entityList) {
            JSONObject entity = new JSONObject();
            entity.put("lastName", n.getLastName());
            entity.put("firstName", n.getFirstName());
            entity.put("birthdate", n.getBirthdate().toString());
            entity.put("mobilePhone", n.getMobilePhone());
            entity.put("workPhone", n.getWorkPhone());
            entity.put("email", n.getEmail());
            entities.add(entity);
        }
        log.info("Результаты поиска флаг 3: 'коллеги по адресу' для сотрудника с id = {}, количество записей: {}", employeeId, entityList.stream().count());
        log.info("Подготовлены следующие данные: {}", entities);
        return entities;
    }

    /**
     * Вставка новой записи в таблицу employee
     * SQL_UPDATE_PROFILE = "insert into employee (first_name, last_name, work_phone, mobile_phone, email, birthdate, job_id) values (:firstName,:lastName, :work_phone, :mobile_phone, :email, :birthdate, :job_id)";
     */
    @PostMapping("/employees")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Создание новой записи")
    public Employee createEmp(@Valid @RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    // Получение одной записи по employee.id
    @GetMapping(value = "/employees/{employeeId}")
    @ApiOperation("Получение одиночной записи по employee.id")
    public Optional<Employee> findEmp(@PathVariable("employeeId") @Min(1) int employeeId) {
        return employeeRepository.findById((long) employeeId);
    }


    // Обновление записи
    // SQL_UPDATE_PROFILE = "update employee set first_name = :firstName, last_name = :lastName, .... where id = :id";
    @PutMapping(value = "/employees/{employeeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Обновление существующей записи")
    public void updateEmp(@PathVariable("employeeId") @Min(1) int employeeId, @Valid @RequestBody Employee empRequest) {
        log.info("Вызов Update Employee employeeId = {}, empRequest = {}", employeeId, empRequest);

        final Optional<Employee> emp = employeeRepository.findById((long) employeeId);
        final Employee empModel = emp.orElseThrow(() -> new ResourceNotFoundException("Сотрудник с идентификатором = {"+employeeId+"} не найден"));

        empModel.setFirstName(empRequest.getFirstName());
        empModel.setLastName(empRequest.getLastName());
        empModel.setWorkPhone(empRequest.getWorkPhone());
        empModel.setMobilePhone(empRequest.getMobilePhone());
        empModel.setEmail(empRequest.getEmail());
        empModel.setBirthdate(empRequest.getBirthdate());

        log.info("Сохранение результата в таблицу employee {}", empModel);
        employeeRepository.save(empModel);
    }

    @DeleteMapping(value = "/employees/{employeeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Удаление записи")
    public void deleteEmp(@PathVariable("employeeId") @Min(1) int employeeId) {
        log.info("Вызов удаление из таблицы Employee для employeeId = {}", employeeId);
        employeeRepository.deleteById((long) employeeId);
    }

}
