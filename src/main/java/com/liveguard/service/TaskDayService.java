package com.liveguard.service;

import com.liveguard.domain.TaskDay;
import com.liveguard.dto.TaskDayDTO;

import java.util.List;

public interface TaskDayService {

    List<TaskDayDTO> findAll();

    TaskDay findById(Long id);
}
