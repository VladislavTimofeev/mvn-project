package com.vlad.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class AspectLogging {

    @Pointcut("execution(public * com.vlad.service.*Service.*(..))")
    public void anyServiceMethod() {
    }

    @Around("anyServiceMethod() && target(service)")
    public Object addLoggingAround(ProceedingJoinPoint joinPoint, Object service) throws Throwable {
        log.info("AROUND before - invoking method: {} with args: {} in class {}",
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()),
                service);
        try {
            Object result = joinPoint.proceed();
            log.info("AROUND after returning - invoked method: {} in class {}, result: {}",
                    joinPoint.getSignature().getName(),
                    service,
                    result);
            return result;
        } catch (Throwable ex) {
            log.error("AROUND after throwing - invoked method: {} in class {}, exception {}: {}",
                    joinPoint.getSignature().getName(),
                    service,
                    ex.getClass(),
                    ex.getMessage(),
                    ex);
            throw ex;
        } finally {
            log.debug("AROUND after (finally) - invoked method: {} in class {}",
                    joinPoint.getSignature().getName(),
                    service);
        }
    }
}
