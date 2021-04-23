package com.sbu.redistrictingserver.repository;

import com.sbu.redistrictingserver.model.Precinct;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PrecinctRepository extends CrudRepository<Precinct, Integer>{
    Optional<Precinct> findById(Integer id);
    List<Precinct> findAll();
}
