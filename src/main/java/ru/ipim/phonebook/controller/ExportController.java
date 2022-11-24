package ru.ipim.phonebook.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.ApiOperation;
import net.minidev.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import liquibase.pro.packaged.T;

import ru.ipim.phonebook.repository.EmployeeRepository;
import ru.ipim.phonebook.repository.JobRepository;

import ru.ipim.phonebook.model.EmpExportType0;
import ru.ipim.phonebook.model.EmpExportType1;
import ru.ipim.phonebook.model.EmpExportType2;
import ru.ipim.phonebook.util.DirSettings;

import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Objects;

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
    // Пример обращения: type=phonebook
    //  http://localhost:8080/export/?type=phonebook&startdate=2022-11-09&enddate=2022-12-12
    //
    // Выгрузка тип 2 : сотрудники с привязкой к местам работы.
    // Поля: фамилия,имя, мобильный телефон, email +  Наименование организации, Должность, Рабочий адрес.
    // Пример обращения: type=employee-job
    //  http://localhost:8080/export/?type=employee-job&startdate=2022-11-09&enddate=2022-12-12
    @GetMapping("/export/")
    @ApiOperation("Выгрузка записей. phonebook = тип выгрузки 1, employee-job = тип выгрузки 2 ")
    public @ResponseBody List<JSONObject>  exportTypesOneAndTwo(@RequestParam String type,@RequestParam(required = false) String  startdate,@RequestParam(required = false) String  enddate) {
      log.info("Выгрузка с type = '{}', startdate = '{}', enddate = '{}'", type, startdate, enddate);
      // результат формирования экспорта
      List<JSONObject> entities = new ArrayList<JSONObject>();

      if (Objects.equals(type, "phonebook")) {
          log.info("Запуск выгрузки тип 1. Телефонная книга");
          List<EmpExportType1> entityList;

          if (StringUtils.isNotBlank(startdate) && StringUtils.isNotBlank(enddate)) {
              log.info("Не пустые startdate = '{}', enddate = '{}'", startdate, enddate);
              // Не задана начальная дата
              if (StringUtils.isBlank(startdate)) {
                  entityList = employeeRepository.exportType1dWithJPQL("0001-01-01", enddate);
              }
              // Не задана конечная дата
              else if (StringUtils.isBlank(enddate)){
                  entityList = employeeRepository.exportType1dWithJPQL(startdate, "9999-12-31");
              }
              // обе даты
              else entityList = employeeRepository.exportType1dWithJPQL(startdate, enddate);
          }
          else {
              // без указания дат - все записи
              entityList = employeeRepository.exportType1WithJPQL();
          }

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
          log.info("Выгрузка тип 1. Телефонная книга с сортировкой по фамилии и имени, количество записей: {}", (long) entityList.size());

      }
      else if (Objects.equals(type, "employee-job")) {
          log.info("Запуск выгрузки тип 1. Отдельные поля Телефонной книги + поля из Место работы");
          List<EmpExportType2> entityList ;

          if (StringUtils.isNotBlank(startdate) && StringUtils.isNotBlank(enddate)) {
              log.info("Не пустые startdate = '{}', enddate = '{}'", startdate, enddate);
              // Не задана начальная дата
              if (StringUtils.isBlank(startdate)) {
                  entityList = employeeRepository.exportType2dWithJPQL("0001-01-01", enddate);
              }
              // Не задана конечная дата
              else if (StringUtils.isBlank(enddate)){
                  entityList = employeeRepository.exportType2dWithJPQL(startdate, "9999-12-31");
              }
              // обе даты
              else entityList = employeeRepository.exportType2dWithJPQL(startdate, enddate);
          }
          else {
              // без указания дат - все записи
              entityList = employeeRepository.exportType2WithJPQL();
          }

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
          log.info("Выгрузка тип 2. Телефонная книга с сортировкой по фамилии и имени + поля из Место работы, количество записей: {}", (long) entityList.size());

      }
      else {
          log.info("Выгрузка неизвестного типа. Переданное в URL значение type= '{}' не может быть обработано. ", type);
          log.info("Допустимые значения: type=phonebook или type=employee-job");
          return entities;
      }
      log.info("Подготовлены следующие данные: {}", entities);

        // сгенерировать имя файла в каталоге для экспорта, если файл уже есть он будет перезаписан
        String filename = settings.getOutFileName().toString();

        // файл данных
        String filetxt = filename + ".txt";
        try (PrintWriter out = new PrintWriter(new FileWriter(filetxt))) {
            out.write(entities.toString());
            log.info("Записан файл данных: {}", filetxt);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // файл флаг пустой
        String fileready = filename + ".ready";
        try (PrintWriter out = new PrintWriter(new FileWriter(fileready))) {
            out.write("");
            log.info("Записан файл-флаг: {}", fileready);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return entities;
    }

    // Записи талицы Employee дополненные местом работы, должностью, адресом, если нет места работы, то запись не выводится
    @GetMapping("/export-test")
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
        log.info("Получение списка всех записей: join employee + job, количество записей: {}", (long) entityList.size());
        log.info("Подготовлены следующие данные: {}", entities);
        return entities;

    }

}
