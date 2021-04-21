package com.sbu.redistrictingserver.repository;

import com.sbu.redistrictingserver.model.Job;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface JobRepository extends CrudRepository<Job, Long> {
    Optional<Job> findById(Long id);
    List<Job> findAll();
}
