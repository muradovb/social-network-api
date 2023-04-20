package com.akamai.socialnetwork.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


@Slf4j
@Aspect
@Component
public class AspectLog {


    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void repositoryClassMethods() {};


    @Before("repositoryClassMethods()")
    public void logEnterMethod(JoinPoint thisJoinPoint) {
        Object[] signatureArgs = thisJoinPoint.getArgs();
        ArrayList<String> list = new ArrayList<>();
        for (Object signatureArg: signatureArgs) {
            try {
                list.add(signatureArg.toString());
            }catch (NullPointerException e){
                list.add("null");
            }

        }
        log.info(thisJoinPoint.getSignature().getName() +" requst value " + list);
    }

    @AfterReturning(pointcut = "repositoryClassMethods()", returning = "result")
    public void logExitMethod(JoinPoint thisJoinPoint, Object result) {
        log.info(thisJoinPoint.getSignature().getName() +" return value = " + result);
    }

    @AfterThrowing(pointcut = "repositoryClassMethods()", throwing = "exception")
    public void logException(JoinPoint thisJoinPoint, Exception exception) {
        log.info(thisJoinPoint.getSignature().getName() + " ERROR  " + exception.getMessage());
    }

}