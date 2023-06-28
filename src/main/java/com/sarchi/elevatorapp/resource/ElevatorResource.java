package com.sarchi.elevatorapp.resource;

import com.sarchi.elevatorapp.domain.ElevatorDto;
import com.sarchi.elevatorapp.domain.ElevatorEntity;
import com.sarchi.elevatorapp.domain.ElevatorRequest;
import com.sarchi.elevatorapp.domain.TransactionLog;
import com.sarchi.elevatorapp.service.ElevatorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api")
@AllArgsConstructor
public class ElevatorResource {
    ElevatorService service;

    @PostMapping("/create")
    public ResponseEntity<ElevatorEntity> createElevator(@RequestBody ElevatorDto dto) {
        var res = service.createElevator(dto);
        if (res == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(res);
    }

    @PostMapping("/request")
    public String requestElevator(@RequestBody ElevatorRequest request) throws Exception {
        return service.requestElevator(request);
    }

    @GetMapping("/logs")
    public ResponseEntity<List<ElevatorEntity>> fetchElevatorStatus() {
        return service.fetchElevatorStatus();
    }
}
