package com.truf.common.advice;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Log4j2
@Component
public class ExecutionTimeTrackerAdvice {

    @Around("@annotation(com.rs.cm.advice.TrackExecutionTime)")
    public Object trackApiTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object object = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        String declaringType = proceedingJoinPoint.getSignature().getDeclaringTypeName();
        String controllerName = declaringType.substring(declaringType.lastIndexOf('.') + 1);
        String methodName = proceedingJoinPoint.getSignature().getName();
        if(executionTime >=500){
            log.info("API Performance Monitor. Method : {}.{}() took {} ms to execute. Please check.",
                    controllerName, methodName, executionTime);
            return object;
        }
        log.info("API method name : {}.{}() took {} ms to execute.",
                controllerName, methodName, executionTime);
        return object;
    }
}
