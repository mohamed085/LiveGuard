package com.liveguard.service.serviceImp;

import com.liveguard.domain.Chip;
import com.liveguard.domain.Task;
import com.liveguard.domain.TaskDay;
import com.liveguard.domain.User;
import com.liveguard.dto.TaskDTO;
import com.liveguard.dto.TaskSimpleDataDTO;
import com.liveguard.dto.UserSimpleDataDTO;
import com.liveguard.exciptions.BadRequestException;
import com.liveguard.exciptions.NotFoundException;
import com.liveguard.mapper.TaskMapper;
import com.liveguard.repository.ChipRepository;
import com.liveguard.repository.TaskDayRepository;
import com.liveguard.repository.TaskRepository;
import com.liveguard.service.TaskService;
import com.liveguard.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

@Slf4j
@Service
public class TaskServiceImp implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final ChipRepository chipRepository;
    private final TaskDayRepository taskDayRepository;

    public TaskServiceImp(TaskRepository taskRepository, UserService userService, ChipRepository chipRepository, TaskDayRepository taskDayRepository) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.chipRepository = chipRepository;
        this.taskDayRepository = taskDayRepository;
    }

    @Override
    public TaskDTO findById(Long id) throws ParseException {
        Task task = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found"));
        return TaskMapper.taskToTaskDTO(task);
    }

    @Override
    public TaskDTO addTask(Long chipId, TaskDTO taskDTO) throws ParseException {
        log.debug("TaskService | addTask | chipId: " + chipId);
        log.debug("TaskService | addTask | taskDTO: " + taskDTO.getName());

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.debug("TaskService | addTask | userEmail: " + userEmail);

        User user = userService.findByEmail(userEmail)
                .orElseThrow(() ->  new NotFoundException("This email not exist"));

        Chip chip = chipRepository.findById(chipId)
                .orElseThrow(() -> new NotFoundException("This chip not found"));
        log.debug("ChipService | addChipAssociatedDetails | chip: " + chip.getName());

        if (chip.getUsers().isEmpty()) {
            log.warn("ChipService | addChipAssociatedDetails | This chip not used yet");
            throw new BadRequestException("This chip not used yet");
        }


        Task task = TaskMapper.taskDTOToTask(taskDTO);

        Set<TaskDay> repeat = new HashSet<>();
        taskDTO.getRepeatId().forEach(dayId -> repeat.add(taskDayRepository.findById(dayId).get()));

        task.setRepeat(repeat);
        task.setMute(false);
        task.setChip(chip);
        task.setAddByUser(user);
        task.setCreateDate(new Date());
        log.debug("ChipService | addChipAssociatedDetails | task: " + task.toString());

        Task savedTask = taskRepository.save(task);
        log.debug("ChipService | addChipAssociatedDetails | savedTask: " + savedTask.toString());


        return TaskMapper.taskToTaskDTO(savedTask);
    }

    @Override
    public List<TaskSimpleDataDTO> findByChipId(Long id) {
        log.debug("TaskService | addTask | chipId: " + id);

        List<Task> tasks = taskRepository.findByChipId(id);
        List<TaskSimpleDataDTO> TaskSimpleDataDTO = new ArrayList<>();
        tasks.forEach(task -> {
            UserSimpleDataDTO addByUser = new UserSimpleDataDTO(task.getAddByUser().getId(), task.getAddByUser().getEmail(), task.getAddByUser().getName(), task.getAddByUser().getAvatar());
            TaskSimpleDataDTO.add(new TaskSimpleDataDTO(task.getId(), task.getName(), task.getMute(), addByUser));
        });
        return TaskSimpleDataDTO;
    }
}
