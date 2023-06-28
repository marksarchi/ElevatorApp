package com.sarchi.elevatorapp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ElevatorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int currentFloor;
    private int destinationFloor;
    private Direction direction;
    private boolean isMoving;
    private DoorStatus doorStatus;

    private int maxFloorLimit;


    public ElevatorEntity() {
        this.currentFloor = 1;
        this.destinationFloor = 1;
        this.direction = Direction.STOPPED;
        this.isMoving = false;
        this.doorStatus = DoorStatus.CLOSED;
    }}
