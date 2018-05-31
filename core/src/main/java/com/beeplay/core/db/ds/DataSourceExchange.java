package com.beeplay.core.db.ds;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
@Component
@Aspect
@Order(1)
public class DataSourceExchange  {
    @Pointcut("execution(* com.beeplay..*.*(..))")
    public void ds(){
    }
    @AfterReturning(returning = "object",pointcut = "ds()")
    public void doAfterReturn(Object object){
        DataSourceContext.clearCustomerType();
    }
    @Before("ds()")
    public void before(JoinPoint joinPoint){
        //这里DataSource是自定义的注解，不是java里的DataSource接口
        Method method=((MethodSignature)joinPoint.getSignature()).getMethod();
        if (method.isAnnotationPresent(DataSource.class)) {
            DataSource datasource = method.getAnnotation(DataSource.class);
            DataSourceContext.setCustomerType(datasource.name());
        } else {
            DataSourceContext.setCustomerType(DataSourceContext.DATA_SOURCE_WRITE);
        }
    }


}  
