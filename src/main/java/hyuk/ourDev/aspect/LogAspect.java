package hyuk.ourDev.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Pointcut("execution(* hyuk.ourDev.domain.board..*(..)) "
        + "&& !execution(* hyuk.ourDev.domain.board.dto..*(..)) "
        + "&& !execution(* hyuk.ourDev.domain.board.entity..*(..))"
        + "&& !execution(* hyuk.ourDev.domain.board.mapper..*(..))")
    public void logging() {}

    @Before("logging()")
    public void beforeMethod(JoinPoint joinpoint) {
        log.info("===================================");
        log.info("[메소드 호출] - {}", joinpoint.getSignature().getName());
    }

    @After("logging()")
    public void afterMethod(JoinPoint joinPoint) {
        log.info("[메소드 종료] - {}", joinPoint.getSignature().getName());
        log.info("===================================");
    }

    @AfterThrowing(pointcut = "execution(* hyuk.ourDev.domain.board..*(..))", throwing = "ex")
    public void afterThrowingMethod(JoinPoint joinPoint, Throwable ex) {
        log.error("[에러 발생] - {}, {}", joinPoint.getSignature().getName(), ex.getMessage());
    }
}
