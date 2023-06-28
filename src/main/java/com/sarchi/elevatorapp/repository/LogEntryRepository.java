package com.sarchi.elevatorapp.repository;

import com.sarchi.elevatorapp.domain.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogEntryRepository  extends JpaRepository<LogEntry,Long> {
}
