package ru.ipim.phonebook.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import liquibase.pro.packaged.T;

import ru.ipim.phonebook.repository.EmployeeRepository;
import ru.ipim.phonebook.repository.JobRepository;

import ru.ipim.phonebook.model.Employee;
import ru.ipim.phonebook.model.EmpExportType0;
import ru.ipim.phonebook.model.EmpExportType1;
import ru.ipim.phonebook.model.EmpExportType2;
import ru.ipim.phonebook.model.StatCompanys;
import ru.ipim.phonebook.util.DirSettings;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.io.FileWriter;
import java.io.PrintWriter;

@RestController
@Slf4j
public class ExportController {
    private final EmployeeRepository employeeRepository;
    private final JobRepository jobRepository;
    // каталоги для экспорта и генерация имен файлов
    @Autowired
    private DirSettings<T> settings;

    ExportController(EmployeeRepository employeeRepository, JobRepository jobRepository) {
        this.employeeRepository = employeeRepository;
        this.jobRepository = jobRepository;
    }

    // Выгрузка тип 1 : Телефонная книга с сортировкой по фамилии и имени
    // поля : Фамилия, Имя, Рабочий телефон, Мобильный телефон, e-mail, Дата рождения
    @GetMapping("/export1")
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

        // сгенерировать имя файла в каталоге для экспорта
        String filename = settings.getOutFileName().toString();

        // файл данных
        String filetxt = filename + ".txt";
        try (PrintWriter out = new PrintWriter(new FileWriter(filetxt))) {
            out.write(entities.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // файл флаг пустой
        String fileready = filename + ".ready";
        try (PrintWriter out = new PrintWriter(new FileWriter(fileready))) {
            out.write("");
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("Выгрузка тип 1. Телефонная книга с сортировкой по фамилии и имени, количество записей: {}", entityList.stream().count());
        log.info("Подготовлены следующие данные: {}", entities);
        return entities;
    }

    // Выгрузка тип 2 : сотрудники с привязкой к местам работы.
    // Поля: фамилия,имя, мобильный телефон, email +  Наименование организации, Должность, Рабочий адрес.
    @GetMapping("/export2")
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

        // сгенерировать имя файла в каталоге для экспорта
        String filename = settings.getOutFileName().toString();

        // файл данных
        String filetxt = filename + ".txt";
        try (PrintWriter out = new PrintWriter(new FileWriter(filetxt))) {
            out.write(entities.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // файл флаг пустой
        String fileready = filename + ".ready";
        try (PrintWriter out = new PrintWriter(new FileWriter(fileready))) {
            out.write("");
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("Выгрузка тип 2. Телефонная книга с сортировкой по фамилии и имени, количество записей: {} - {}", entityList.stream().count());
        log.info("Подготовлены следующие данные: {}", entities);
        return entities;
    }

    // Все записи талицы Employee дополненные местом работы, должностью, адресом
    @GetMapping("/export3")
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
        log.info("Получение списка всех записей: join employee + job, количество записей: {}", entityList.stream().count());
        log.info("Подготовлены следующие данные: {}", entities);
        return entities;

    }

}
