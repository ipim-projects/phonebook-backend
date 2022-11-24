package ru.ipim.phonebook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.minidev.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import liquibase.pro.packaged.T;

import ru.ipim.phonebook.repository.EmployeeRepository;
import ru.ipim.phonebook.repository.JobRepository;

import ru.ipim.phonebook.model.Employee;
import ru.ipim.phonebook.model.StatCompanys;
import ru.ipim.phonebook.util.DirSettings;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class ReportController {
    private final EmployeeRepository employeeRepository;
    private final JobRepository jobRepository;

    ReportController(EmployeeRepository employeeRepository, JobRepository jobRepository) {
        this.employeeRepository = employeeRepository;
        this.jobRepository = jobRepository;
    }

    @GetMapping("/reports/employee/count-all")
    @ApiOperation("Статистика - Количество записей в таблице employee")
    public long countAllEmployees() {
        return employeeRepository.count();
    }

    @GetMapping("/reports/job/count-all")
    @ApiOperation("Статистика - Количество записей в таблице job")
    public long countAllJobs() {
        return jobRepository.count();
    }

    @GetMapping("/reports/employee/count-month")
    @ApiOperation("Статистика - Количество записей в таблице employee за месяц")
    public long countEmployeesForMonth() {
        return employeeRepository.countForMonth();
    }

    // Статистика 'общее количество коллег по должности, организации, и адресу работы'
    @GetMapping("/reports/employee/statistic")
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


}
