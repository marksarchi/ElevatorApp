package com.sarchi.elevatorapp.service;

import com.sarchi.elevatorapp.domain.DoorStatus;
import com.sarchi.elevatorapp.domain.*;
import com.sarchi.elevatorapp.exceptions.BadRequestException;
import com.sarchi.elevatorapp.exceptions.NotFoundException;
import com.sarchi.elevatorapp.repository.ElevatorRepository;
import com.sarchi.elevatorapp.repository.LogEntryRepository;
import com.sarchi.elevatorapp.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ElevatorService {
    private static final Logger log = LoggerFactory.getLogger(ElevatorService.class);

    ElevatorRepository repository;
    LogEntryRepository logEntryRepository;
    TransactionRepository transactionRepository;

    public ElevatorEntity createElevator(ElevatorDto dto) {
        ElevatorEntity entity = new ElevatorEntity();
        entity.setMaxFloorLimit(dto.getMaximumFloor());
        return repository.save(entity);
    }

    public String requestElevator(ElevatorRequest request) throws Exception {

        var elevator = repository.findById(request.getElevatorId());

        if (elevator.isEmpty()) {
            return "Requested elevator not found";
        }
        if (request.getFromFloor() > elevator.get().getMaxFloorLimit() ||
                request.getDestinationFloor() > elevator.get().getMaxFloorLimit()
        ) {
            return "Requested floor is beyond elevator limit";

        }

        if (request.getFromFloor() == elevator.get().getCurrentFloor()) {
            openElevator(elevator.get());
        }

        moveElevator(request, elevator.get());
        return "Elevator requested successfully";

    }

    private void moveElevator(ElevatorRequest request, ElevatorEntity elevator) throws InterruptedException {
        // Move elevator to requested floor
        var direction = request.getFromFloor() > elevator.getCurrentFloor() ? Direction.UP : Direction.DOWN;
        elevator.setDirection(direction);
        moveElevatorToRequestedFloor1(request, elevator);
        // Move elevator to destination floor
        elevator.setCurrentFloor(request.getFromFloor());
        elevator.setDirection(request.getDestinationFloor() > elevator.getCurrentFloor() ? Direction.UP : Direction.DOWN);
        repository.save(elevator);
        moveElevatorToDestinationFloor(request, elevator);
    }

    @Async
    public void moveElevatorToDestinationFloor(ElevatorRequest request, ElevatorEntity elevator) throws InterruptedException {
        log.info("<<<<<<<<ELEVATOR {} IS MOVING FROM FLOOR {} TO DESTINATION FLOOR {} >>>>>>>>>>>",
                elevator.getId(), elevator.getCurrentFloor(), request.getDestinationFloor());

        if (elevator.getDirection().equals(Direction.UP)) {
            for (int i = elevator.getCurrentFloor(); i <= request.getDestinationFloor(); i++) {
                updateElevatorStatus(elevator, i, request.getDestinationFloor());
            }
        }

        if (elevator.getDirection().equals(Direction.DOWN)) {
            for (int i = elevator.getCurrentFloor(); i >= request.getDestinationFloor(); i--) {
                updateElevatorStatus(elevator, i, request.getDestinationFloor());
            }
        }
    }

    private void moveElevatorToRequestedFloor1(ElevatorRequest request, ElevatorEntity elevator) throws InterruptedException {
        log.info("<<<<<<<<ELEVATOR {} IS MOVING FROM FLOOR {} TO REQUESTED FLOOR {} >>>>>>>>>>>",
                elevator.getId(), elevator.getCurrentFloor(), request.getFromFloor());

        if (elevator.getDirection().equals(Direction.UP)) {
            for (int i = elevator.getCurrentFloor(); i <= request.getFromFloor(); i++) {
                updateElevatorStatus(elevator, i, request.getFromFloor());
            }
        }

        if (elevator.getDirection().equals(Direction.DOWN)) {
            for (int i = elevator.getCurrentFloor(); i >= request.getFromFloor(); i--) {
                updateElevatorStatus(elevator, i, request.getFromFloor());
            }
        }

    }


    private void updateElevatorStatus(ElevatorEntity elevator, Integer currentFloor, Integer requestedFloor) throws InterruptedException {
        elevator.setMoving(true);
        repository.save(elevator);
        Thread.sleep(5000);
        log.info("<<<<<<<<<ELEVATOR {} IS  AT FLOOR {}>>>>>>>>", elevator.getId(), currentFloor);
        elevator.setCurrentFloor(currentFloor);
        repository.save(elevator);

        if (currentFloor == requestedFloor) {
            log.info("<<<<<<<<ELEVATOR {} HAS ARRIVED AT FLOOR {} , OPENING THE DOOR>>>>>>>>", elevator.getId(), requestedFloor);
            log.info("<<<<<<<<OPENING THE DOOR>>>>>>>>");
            elevator.setMoving(false);
            elevator.setDirection(Direction.STOPPED);
            repository.save(elevator);
            openElevator(elevator);
        }
    }

    @Async
    public void openElevator(ElevatorEntity elevator) throws InterruptedException {
        Thread.sleep(2000);
        elevator.setDoorStatus(DoorStatus.OPEN);
        log.info("<<<<<<<ELEVATOR: {} DOOR IS OPEN>>>>>>>", elevator.getId());
        repository.save(elevator);
        closeElevator(elevator);
    }

    @Async
    public void closeElevator(ElevatorEntity elevator) throws InterruptedException {

        Thread.sleep(2000);
        elevator.setDoorStatus(DoorStatus.CLOSED);
        log.info("<<<<<<<ELEVATOR: {} DOOR IS CLOSED>>>>>>>", elevator.getId());

        repository.save(elevator);
    }

    public ResponseEntity <List<ElevatorEntity>> fetchElevatorStatus() {
        var elevators = repository.findAll();
        if (elevators.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(elevators);
    }
}
