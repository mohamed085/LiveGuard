package com.liveguard.service.serviceImp;

import com.liveguard.domain.TaskDay;
import com.liveguard.dto.TaskDayDTO;
import com.liveguard.exciptions.NotFoundException;
import com.liveguard.mapper.TaskDayMapper;
import com.liveguard.repository.TaskDayRepository;
import com.liveguard.service.TaskDayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TaskDayServiceImp implements TaskDayService {

    private final TaskDayRepository taskDayRepository;

    public TaskDayServiceImp(TaskDayRepository taskDayRepository) {
        this.taskDayRepository = taskDayRepository;
    }

    @Override
    public List<TaskDayDTO> findAll() {
        log.debug("TaskDayService | findAll");
        List<TaskDay> taskDays = (List<TaskDay>) taskDayRepository.findAll();
        List<TaskDayDTO> taskDayDTOs = new ArrayList<>();
        taskDays.forEach(taskDay -> taskDayDTOs.add(TaskDayMapper.taskDayToTaskDayDTO(taskDay)));
        return taskDayDTOs;
    }

    @Override
    public TaskDay findById(Long id) {
        log.debug("TaskDayService | findById | id: " + id);
        return taskDayRepository.findById(id).orElseThrow(() -> new NotFoundException("This day not found"));
    }
}
