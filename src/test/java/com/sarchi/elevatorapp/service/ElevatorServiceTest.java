package com.sarchi.elevatorapp.service;

import com.sarchi.elevatorapp.domain.ElevatorDto;
import com.sarchi.elevatorapp.domain.ElevatorEntity;
import com.sarchi.elevatorapp.domain.ElevatorRequest;
import com.sarchi.elevatorapp.repository.ElevatorRepository;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ElevatorServiceTest {
    @MockBean
    ElevatorRepository elevatorRepository;
    @MockBean
    ElevatorService elevatorService;

//    @Test
    void createElevator() {
        ElevatorDto elevator = new ElevatorDto();
        elevator.setMaximumFloor(50);
        ElevatorEntity entity = new ElevatorEntity();
        entity.setMaxFloorLimit(elevator.getMaximumFloor());
        entity.setId(RandomUtils.nextLong());
        ElevatorEntity  res = elevatorService.createElevator(elevator);
        when(elevatorRepository.save(Mockito.any())).thenReturn(entity);

        assertEquals(res.getId(),entity.getId());


    }

   // @Test
    void requestElevator() {
    }
}