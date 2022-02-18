package com.liveguard.repository;

import com.liveguard.domain.Chip;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
public interface ChipRepository extends CrudRepository<Chip, Long> {
}
