package com.sarchi.elevatorapp.repository;

import com.sarchi.elevatorapp.domain.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionLog, Long> {

}
