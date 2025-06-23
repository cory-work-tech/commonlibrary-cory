package cory.sakti.commonlibrarycory.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerPointcut() {}

    @Around("restControllerPointcut()")
    public Object logApiExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] methodArgs = joinPoint.getArgs();

        logger.info("==> Starting execution of [{}::{}] with arguments: {}", className, methodName, Arrays.toString(methodArgs));

        long startTime = System.currentTimeMillis();
        Object result;

        try {
            result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            logger.info("<== Finished execution of [{}::{}] in {} ms", className, methodName, executionTime);

        } catch (Throwable t) {
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            logger.error("<== Failed execution of [{}::{}] in {} ms. Exception: {}: {}",
                    className, methodName, executionTime, t.getClass().getSimpleName(), t.getMessage());

            throw t;
        }

        return result;
    }
}
