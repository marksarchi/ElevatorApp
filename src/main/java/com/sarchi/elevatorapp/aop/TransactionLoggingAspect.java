package com.sarchi.elevatorapp.aop;

import com.sarchi.elevatorapp.domain.TransactionLog;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
@AllArgsConstructor
public class TransactionLoggingAspect {
    JdbcTemplate template;


    @Around("execution(* com.sarchi.elevatorapp.repository.*.save(..))")

    public Object logTransaction(ProceedingJoinPoint joinPoint) throws Throwable{
        String transactionType = joinPoint.getSignature().getName();
        LocalDateTime transactionTime = LocalDateTime.now();
        String transactionData = Arrays.toString(joinPoint.getArgs());

        // Save transaction log to the database
        TransactionLog transactionLog = new TransactionLog();
        transactionLog.setTransactionType(transactionType);
        transactionLog.setTransactionTime(transactionTime);
        transactionLog.setTransactionData(transactionData);

        // Use a different save method to avoid interception by the aspect
        // Save transaction log to the database
        String sql = "INSERT INTO transaction_log (transaction_type, transaction_time, transaction_data) VALUES (?, ?, ?)";
        template.update(sql, transactionType, transactionTime, transactionData);

        // Proceed with the original method execution
        return joinPoint.proceed();
    }
}

