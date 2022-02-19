package com.liveguard.mapper;

import com.liveguard.domain.Task;
import com.liveguard.domain.TaskDay;
import com.liveguard.dto.ChipSimpleDataDTO;
import com.liveguard.dto.TaskDTO;
import com.liveguard.dto.TaskDayDTO;
import com.liveguard.dto.UserSimpleDataDTO;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class TaskMapper {

    public static Task taskDTOToTask(TaskDTO taskDTO) {
        Task task = new Task();
        task.setName(taskDTO.getName());
        task.setDescription(taskDTO.getDescription());
        task.setRingtone(taskDTO.getRingtone());
        task.setLat(taskDTO.getLat());
        task.setLng(taskDTO.getLng());
        task.setArea(taskDTO.getArea());
        task.setStartDate(taskDTO.getStartDate());
        task.setEndDate(taskDTO.getEndDate());

        return task;
    }

    public static TaskDTO taskToTaskDTO(Task task) {
        List<TaskDayDTO> repeat = new ArrayList<>();
        task.getRepeat().forEach(taskDay -> repeat.add(TaskDayMapper.taskDayToTaskDayDTO(taskDay)));

        ChipSimpleDataDTO chip = new ChipSimpleDataDTO(task.getChip().getId(), task.getChip().getName(),
                task.getChip().getPhoto());

        UserSimpleDataDTO addByUser = new UserSimpleDataDTO(task.getAddByUser().getId(),
                task.getAddByUser().getEmail(), task.getAddByUser().getName(), task.getAddByUser().getAvatar());

        String pattern = "dd-M-yyyy hh:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(pattern);


        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(task.getId());
        taskDTO.setName(task.getName());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setRingtone(task.getRingtone());
        taskDTO.setLat(task.getLat());
        taskDTO.setLng(task.getLng());
        taskDTO.setArea(task.getArea());
        taskDTO.setMute(task.getMute());

        log.debug(task.getStartDateInString());
        log.debug(task.getEndDateInString());
        log.debug(task.getCreateDateInString());

        try {
            taskDTO.setStartDate(format.parse(task.getStartDateInString()));
            log.debug("TaskMapper | taskToTaskDTO | start date: " + format.parse(task.getStartDateInString()));
        } catch (ParseException e) {
            log.warn("TaskMapper | taskToTaskDTO | failed to parse start date");
            taskDTO.setStartDate(task.getStartDate());
        }
        try {
            taskDTO.setEndDate(format.parse(task.getEndDateInString()));
            log.debug("TaskMapper | taskToTaskDTO | end date: " + format.parse(task.getEndDateInString()));
        } catch (ParseException e) {
            log.warn("TaskMapper | taskToTaskDTO | failed to parse end date");
            taskDTO.setEndDate(task.getEndDate());
        }
        try {
            taskDTO.setCreateDate(format.parse(task.getCreateDateInString()));
            log.debug("TaskMapper | taskToTaskDTO | create date: " + format.parse(task.getCreateDateInString()));
        } catch (ParseException e) {
            log.warn("TaskMapper | taskToTaskDTO | failed to parse create date");
            taskDTO.setCreateDate(task.getCreateDate());
        }

        taskDTO.setRepeat(repeat);
        taskDTO.setChip(chip);
        taskDTO.setAddByUser(addByUser);

        return taskDTO;
    }
}
