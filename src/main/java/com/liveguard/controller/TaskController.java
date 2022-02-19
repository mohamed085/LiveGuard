package com.liveguard.controller;

import com.liveguard.dto.TaskDTO;
import com.liveguard.dto.TaskSimpleDataDTO;
import com.liveguard.payload.ApiResponse;
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

    @GetMapping("/user/chip/{id}")
    public ResponseEntity<?> getAllUserChipTasks(@PathVariable("id") Long id) {
        log.debug("TaskController | getAllUserChipTasks | chip id: " + id);

        List<TaskSimpleDataDTO> taskSimpleDataDTOs = taskService.findByChipIdAndUser(id);
        return ResponseEntity
                .ok()
                .body(taskSimpleDataDTOs);
    }

    @GetMapping("/{userId}/chip/{id}")
    public ResponseEntity<?> getAllSpecificUserChipTasks(@PathVariable("id") Long id,
                                                         @PathVariable("userId") Long userId) {

        log.debug("TaskController | getAllSpecificUserChipTasks | chip id: " + id);
        log.debug("TaskController | getAllSpecificUserChipTasks | user id: " + userId);

        List<TaskSimpleDataDTO> taskSimpleDataDTOs = taskService.findByChipIdAndUser(id);
        return ResponseEntity
                .ok()
                .body(taskSimpleDataDTOs);
    }

    @GetMapping("/{id}/{mute}")
    private ResponseEntity<?> editMuteMode(@PathVariable("id") Long id,
                                           @PathVariable("mute") Boolean muteStatus) {

        log.debug("TaskController | getAllChipTasks | task id: " + id);
        log.debug("TaskController | getAllChipTasks | mute value: " + muteStatus);

        taskService.updateMuteStatus(id, muteStatus);
        return ResponseEntity
                .ok()
                .body(new ApiResponse(true, "Status updated successfully"));
    }

}
