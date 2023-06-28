package com.sarchi.elevatorapp.aop;

import com.sarchi.elevatorapp.domain.LogEntry;
import com.sarchi.elevatorapp.repository.LogEntryRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    @Autowired
    private LogEntryRepository logEntryRepository;
    private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);


    //@After("execution(* org.slf4j.Logger.info(String)) && args(logMessage)")
    @Around("execution(* org.slf4j.Logger.info(..))")
    public void logAfterInfo(ProceedingJoinPoint joinPoint) {
        LogEntry logEntry = new LogEntry();
        String data = Arrays.toString(joinPoint.getArgs());

        logEntry.setMessage(data);

        logEntryRepository.save(logEntry);
        logger.info("Log entry saved to the database: {}", logEntry);
    }
}
