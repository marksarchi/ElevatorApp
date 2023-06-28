package com.sarchi.elevatorapp.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ElevatorRequest {

    Integer fromFloor;
    Integer destinationFloor;
    Long elevatorId;
}
