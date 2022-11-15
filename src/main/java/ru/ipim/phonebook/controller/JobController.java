package ru.ipim.phonebook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.ipim.phonebook.model.Job;
import ru.ipim.phonebook.repository.JobRepository;

import java.util.List;

@RestController
public class JobController {
    private final JobRepository jobRepository;

    @Autowired
    JobController(JobRepository repository) {
        this.jobRepository = repository;
    }

    @GetMapping("/jobs")
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @PostMapping("/jobs")
    Job createOrSaveJob(@RequestBody Job newJob) {
        return jobRepository.save(newJob);
    }
}