package com.sbu.redistrictingserver.repository;

import com.sbu.redistrictingserver.model.BW;
import com.sbu.redistrictingserver.model.Job;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface BWRepository extends CrudRepository<BW, Long> {
    Optional<BW> findById(Long id);
    List<BW> findAll();
}
