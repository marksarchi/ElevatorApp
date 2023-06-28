package com.sarchi.elevatorapp.repository;

import com.sarchi.elevatorapp.domain.ElevatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElevatorRepository extends JpaRepository<ElevatorEntity,Long> {
}
