package com.liveguard.controller;

import com.liveguard.dto.TaskDTO;
import com.liveguard.dto.TaskSimpleDataDTO;
import com.liveguard.service.TaskDayService;
import com.liveguard.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskDayService taskDayService;
    public final TaskService taskService;

    public TaskController(TaskDayService taskDayService, TaskService taskService) {
        this.taskDayService = taskDayService;
        this.taskService = taskService;
    }

    @GetMapping("/days")
    public ResponseEntity<?> getAllDays() {
        log.debug("TaskController | getAllDays");
        return ResponseEntity
                .ok()
                .body(taskDayService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTask(@PathVariable ("id") Long id) throws ParseException {
        log.debug("TaskController | getTask | task id: " + id);

        TaskDTO returnTaskDTO = taskService.findById(id);
        return  ResponseEntity
                .ok()
                .body(returnTaskDTO);
    }

    @PostMapping("/chip/{id}")
    public ResponseEntity<?> addTaskToChip(@PathVariable("id") Long id,
                                           @RequestBody TaskDTO taskDTO) throws ParseException {

        log.debug("TaskController | addTaskToChip | chip id: " + id);
        log.debug("TaskController | addTaskToChip | task name: " + taskDTO.getName());

        TaskDTO returnTaskDTO = taskService.addTask(id, taskDTO);
        return ResponseEntity
                .ok()
                .body(returnTaskDTO);
    }

    @GetMapping("/chip/{id}")
    public ResponseEntity<?> getAllChipTasks(@PathVariable("id") Long id) {
        log.debug("TaskController | getAllChipTasks | chip id: " + id);
        List<TaskSimpleDataDTO> taskSimpleDataDTOs = taskService.findByChipId(id);

        return ResponseEntity
                .ok()
                .body(taskSimpleDataDTOs);
    }

}
