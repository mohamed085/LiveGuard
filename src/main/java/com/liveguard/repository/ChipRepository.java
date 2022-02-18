package com.liveguard.repository;

import com.liveguard.domain.Chip;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping
public interface ChipRepository extends CrudRepository<Chip, Long> {

    List<Chip> findByChipTypeId(Long chipTypeId);
}
