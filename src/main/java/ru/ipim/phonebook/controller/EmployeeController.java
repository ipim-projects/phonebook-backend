package ru.ipim.phonebook.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.minidev.json.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.ipim.phonebook.repository.EmployeeRepository;
import ru.ipim.phonebook.repository.JobRepository;

import ru.ipim.phonebook.model.Job;

import ru.ipim.phonebook.model.Employee;
import ru.ipim.phonebook.model.EmpExportType0;
import ru.ipim.phonebook.model.EmpExportType1;
import ru.ipim.phonebook.model.EmpExportType2;
import ru.ipim.phonebook.model.StatCompanys;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.io.IOException;
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

    // Read List of Employees
    @GetMapping("/employees")
    @ApiOperation("Получение списка всех записей")
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    // Read List of Employees
    @GetMapping("/employees-jobs")
    @ApiOperation("Получение списка всех записей: join employee + job. Сортировка по Фамилии и имени")
    public @ResponseBody List<JSONObject> findEmployeesJobs() {
        List<EmpExportType0> entityList = employeeRepository.exportAllWithJPQL();

        List<JSONObject> entities = new ArrayList<JSONObject>();
        for (EmpExportType0 n : entityList) {
            JSONObject entity = new JSONObject();
            entity.put("lastName", n.getLastName());
            entity.put("firstName", n.getFirstName());
            entity.put("birthdate", n.getBirthdate().toString());
            entity.put("mobilePhone", n.getMobilePhone());
            entity.put("workPhone", n.getWorkPhone());
            entity.put("email", n.getEmail());
            entity.put("company", n.getCompany());
            entity.put("jobTitle", n.getJobTitle());
            entity.put("address", n.getAddress());
            entities.add(entity);
        }
        log.info("Выгрузка тип 1. Телефонная книга с сортировкой по фамилии и имени, количество записей: {}", entityList.stream().count());
        log.info("Подготовлены следующие данные: {}", entities);
        return entities;

    }

    @GetMapping("/employees-statistic")
    @ApiOperation("Статистика - Количество записей в справочнике")
    public String stat(){
        employeeRepository.findAll();
        return ("Record count in phonebook.employee: "+employeeRepository.count()) ;
    }

    // Статистика 'общее количество коллег по должности, организации, и адресу работы'
    @GetMapping("/employees-statistic2")
    @ApiOperation("Статистика по таблице Телефонный справочник с агрегацией числа сотрудников с одинаковым job_id с сортировкой по убыванию количества")
    public @ResponseBody List<JSONObject>  statCompany() {
        List<StatCompanys> entityList = employeeRepository.findGroupByCompanysWithJPQL();

        List<JSONObject> entities = new ArrayList<JSONObject>();
        for (StatCompanys n : entityList) {
            JSONObject entity = new JSONObject();
            entity.put("company", n.getCompany()); // Наименование организации
            entity.put("count",  n.getCnt());  // Количество сотрудников
            entities.add(entity);
        }
        log.info("Количество записей в результате запроса: {}", entities.stream().count());
        log.info("Подготовлены следующие данные: {}", entities);

        return entities;
    }


//    @PostMapping("/employees")
//    Employee createOrSaveEmployee(@RequestBody Employee newEmployee) {
//        Job job = newEmployee.getJobId();
//        if (job != null && job.getId() != null) {
//            job = jobRepository.findById(job.getId()).get();
//            newEmployee.setJobId(job);
//        }
//        return employeeRepository.save(newEmployee);
//    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Веб-форма поиска коллег по выбранному абоненту, в зависимости от выбранного флага:
    // 1. Флаг  - "сотрудники одной и той же с ним организации"
    @GetMapping(value = "/coworkers-company/{empId}")
    @ApiOperation("Поиск коллег по входному параметру (employee.id): сотрудники одной и той же с ним организации")
    public @ResponseBody List<JSONObject> findCoworkersCompany(@PathVariable("empId") @Min(1) int empId) {
        List<EmpExportType1> entityList = employeeRepository.findCoworkersCompanyWithJPQL((long) empId);

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
        log.info("Результаты поиска флаг 1: 'коллеги по организации' для сотрудника с id = {}, количество записей: {}", empId, entityList.stream().count());
        log.info("Подготовлены следующие данные: {}", entities);
        return entities;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Веб-форма поиска коллег по выбранному абоненту, в зависимости от выбранного флага:
    // 2. Флаг  - "сотрудники из разных организаций, но с такой же должностью"
    @GetMapping(value = "/coworkers-job/{empId}")
    @ApiOperation("Поиск коллег - сотрудники из разных организаций, но с такой же должностью")
    public @ResponseBody List<JSONObject> findCoworkersJobTitle(@PathVariable("empId") @Min(1) int empId) {
        List<EmpExportType1> entityList = employeeRepository.findCoworkersJobTitleWithJPQL((long) empId);

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
        log.info("Результаты поиска флаг 2: 'коллеги по должности' для сотрудника с id = {}, количество записей: {}", empId, entityList.stream().count());
        log.info("Подготовлены следующие данные: {}", entities);
        return entities;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Веб-форма поиска коллег по выбранному абоненту, в зависимости от выбранного флага:
    // 3. Флаг  - "сотрудники из разных организаций, но работающих с ним по одному и тому же адресу"
    @GetMapping(value = "/coworkers-address/{empId}")
    @ApiOperation("Поиск коллег - сотрудники из разных организаций, но работающих с ним по одному и тому же адресу")
    public @ResponseBody List<JSONObject> findCoworkersAddress(@PathVariable("empId") @Min(1) int empId) {
        List<EmpExportType1> entityList = employeeRepository.findCoworkersAddressWithJPQL((long) empId);

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
        log.info("Результаты поиска флаг 3: 'коллеги по адресу' для сотрудника с id = {}, количество записей: {}", empId, entityList.stream().count());
        log.info("Подготовлены следующие данные: {}", entities);
        return entities;
    }

    /**
     * Вставка новой записи в таблицу employee
     * SQL_UPDATE_PROFILE = "insert into employee (first_name,last_name, work_phone, mobile_phone, email, birthdate, job_id) values (:firstName,:lastName, :work_phone, :mobile_phone, :email, :birthdate, :job_id)";
     */
    @PostMapping("/employee-add")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Создание новой записи")
    public Employee createEmp(@Valid @RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    /**
     * Read single Employee
     */
    @GetMapping(value = "/employees/{empId}")
    @ApiOperation("Получение одиночной записи по employee.id")
    public Optional<Employee> findEmp(@PathVariable("empId") @Min(1) int empId) {
        return employeeRepository.findById((long) empId);
    }

    // Выгрузка тип 1 : Телефонная книга с сортировкой по фамилии и имени
    // поля : Фамилия, Имя, Рабочий телефон, Мобильный телефон, e-mail, Дата рождения
    @GetMapping("/employees/export1")
    @ApiOperation("Экспорт всех записей, тип экспорта 1. Телефонная книга с сортировкой по фамилии и имени")
    public @ResponseBody List<JSONObject>  exportType1() {
        List<EmpExportType1> entityList = employeeRepository.exportType1WithJPQL();

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
        log.info("Выгрузка тип 1. Телефонная книга с сортировкой по фамилии и имени, количество записей: {}", entityList.stream().count());
        log.info("Подготовлены следующие данные: {}", entities);
        return entities;
    }

    // Выгрузка тип 2 : сотрудники с привязкой к местам работы.
    // Поля: фамилия,имя, мобильный телефон, email +  Наименование организации, Должность, Рабочий адрес.
    @GetMapping("/employees/export2")
    @ApiOperation("Экспорт всех записей, тип экспорта 2")
    public @ResponseBody List<JSONObject>  exportType2() {
        List<EmpExportType2> entityList = employeeRepository.exportType2WithJPQL();

        List<JSONObject> entities = new ArrayList<JSONObject>();
        for (EmpExportType2 n : entityList) {
            JSONObject entity = new JSONObject();
            entity.put("lastName", n.getLastName());
            entity.put("firstName", n.getFirstName());
            entity.put("mobilePhone", n.getMobilePhone());
            entity.put("email", n.getEmail());
            entity.put("company", n.getCompany());
            entity.put("jobTitle", n.getJobTitle());
            entity.put("address", n.getAddress());
            entities.add(entity);
        }
        log.info("Выгрузка тип 2. Телефонная книга с сортировкой по фамилии и имени, количество записей: {} - {}", entityList.stream().count());
        log.info("Подготовлены следующие данные: {}", entities);
        return entities;
    }

    /**
     * Update Employee
     * SQL_UPDATE_PROFILE = "update employee set first_name = :firstName, last_name = :lastName, .... where id = :id";
     */
    @PutMapping(value = "/employee-update/{empId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Обновление существующей записи")
    public void updateEmp(@PathVariable("empId") @Min(1) int empId, @Valid @RequestBody Employee empRequest) {
        log.info("Вызов Update Employee empId = {}, empRequest = {}", empId, empRequest);

        final Optional<Employee> emp = employeeRepository.findById((long) empId);
        final Employee empModel = emp.orElseThrow(() -> new ResourceNotFoundException("Сотрудник с идентификатором = {"+empId+"} не найден"));

        empModel.setFirstName(empRequest.getFirstName());
        empModel.setLastName(empRequest.getLastName());
        empModel.setWorkPhone(empRequest.getWorkPhone());
        empModel.setMobilePhone(empRequest.getMobilePhone());
        empModel.setEmail(empRequest.getEmail());
        empModel.setBirthdate(empRequest.getBirthdate());

        log.info("Сохранение результата в таблицу employee {}", empModel);
        employeeRepository.save(empModel);
    }

    @DeleteMapping(value = "/employee-delete/{empId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Удаление записи")
    public void deleteEmp(@PathVariable("empId") @Min(1) int empId) {
        log.info("Вызов удаление из таблицы Employee для empId = {}", empId);
        employeeRepository.deleteById((long) empId);
    }


}