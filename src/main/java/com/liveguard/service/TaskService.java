package com.liveguard.service;

import com.liveguard.dto.TaskDTO;
import com.liveguard.dto.TaskSimpleDataDTO;

import java.text.ParseException;
import java.util.List;

public interface TaskService {

    TaskDTO findById(Long id) throws ParseException;

    TaskDTO addTask(Long chipId, TaskDTO taskDTO) throws ParseException;

    List<TaskSimpleDataDTO> findByChipId(Long id);

    List<TaskSimpleDataDTO> findByChipIdAndUser(Long id);

    List<TaskSimpleDataDTO> findByChipIdAndSpecificUser(Long userId, Long chipId);

    void updateMuteStatus(Long id, Boolean muteStatus);
}
