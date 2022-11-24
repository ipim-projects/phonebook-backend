package ru.ipim.phonebook.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.minidev.json.JSONObject;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.ipim.phonebook.exception.ResourceNotFoundException;
import ru.ipim.phonebook.model.Job;
import ru.ipim.phonebook.model.StatAddress;
import ru.ipim.phonebook.model.StatCompany;
import ru.ipim.phonebook.repository.JobRepository;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping("/jobs")
@RestController
@RequiredArgsConstructor
@Slf4j
@Api("Контроллер работы с таблицей 'Справочник место работы'")
public class JobController {

    private final JobRepository jobRepository;

    //@Autowired
    //JobController(JobRepository repository) {
    //    this.jobRepository = repository;
    //}

    @GetMapping("/statistic")
    @ApiOperation("Статистика - Количество записей в справочнике Job")
    public String stat(){
        return ("Count of record in table 'job': "+jobRepository.count()) ;
    }

    @GetMapping("/statistic1")
    @ApiOperation("Статистика по таблице место работы с агрегацией по компаниям с сортировкой по убыванию по числу коvпаний")
    public @ResponseBody List<JSONObject>  statCompany() {
        List<StatCompany> entityList = jobRepository.findGroupByCompanyWithJPQL();

        List<JSONObject> entities = new ArrayList<JSONObject>();
        for (StatCompany n : entityList) {
            JSONObject entity = new JSONObject();
            entity.put("company", n.getCompany());        // Наименование организации
            entity.put("count",  n.getCnt().toString());  // Количество
            entities.add(entity);
        }
        log.info("Количество записей в результате запроса: {}", entities.stream().count());
        log.info("Подготовлены следующие данные: {}", entities);

        return entities;
    }

    @GetMapping("/statistic2")
    @ApiOperation("Статистика по таблице место работы с агрегацией по адресу с сортировкой по убыванию по числу одинаковых адресов")
    public @ResponseBody List<JSONObject>  statAddress() {
        List<StatAddress> entityList = jobRepository.findGroupByAddressWithJPQL();

        List<JSONObject> entities = new ArrayList<JSONObject>();
        for (StatAddress n : entityList) {
            JSONObject entity = new JSONObject();
            entity.put("address", n.getAddress());              // Адрес
            entity.put("count",  n.getCnt().toString());        // Количество
            entities.add(entity);
        }
        log.info("Количество записей в результате запроса: {}", entities.stream().count());
        log.info("Подготовлены следующие данные: {}", entities);

        return entities;
    }

    /**
     * Insert record of Job
     * SQL_UPDATE_PROFILE = "insert into job (compan, job_title, address) values (:company, :jobTitle,:address)";
     */
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Создание новой записи")
    public Job createJob(@Valid @RequestBody Job job) {
        return jobRepository.save(job);
    }

    /**
     * Read single Job
     */
    @GetMapping(value = "/{jobId}")
    @ApiOperation("Получение записи по job.id")
    public Optional<Job> findJob(@PathVariable("jobId") @Min(1) int jobId) {
        return jobRepository.findById((long) jobId);
    }

    /**
     * Read List of Jobs
     */
    @GetMapping
    @ApiOperation("Получение списка всех записей")
    public List<Job> findAll() {
        return jobRepository.findAll();
    }

    /**
     * Update Job
     * SQL_UPDATE_PROFILE = "update job set company = :company, job_title = :jobTitle, address = :address where id = :id";
     */
    @PutMapping(value = "/update/{jobId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Обновление существующей записи")
    public void updateJob(@PathVariable("jobId") @Min(1) int jobId, @Valid @RequestBody Job jobRequest) {
        log.info("Вызов Update Job jobId = {}, jobRequest = {}", jobId, jobRequest);

        final Optional<Job> job = jobRepository.findById((long) jobId);
        final Job jobModel = job.orElseThrow(() -> new ResourceNotFoundException("Job "+jobId+" not found"));

        jobModel.setCompany(jobRequest.getCompany());
        jobModel.setJobTitle(jobRequest.getJobTitle());
        jobModel.setAddress(jobRequest.getAddress());
        log.info("Saving job {}", jobModel);
        jobRepository.save(jobModel);
    }

    @DeleteMapping(value = "/delete/{jobId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Удаление записи")
    public void deleteJob(@PathVariable("jobId") @Min(1) int jobId) {
        log.info("Вызов Delete Job jobId = {}", jobId);
        jobRepository.deleteById((long) jobId);
    }

}
