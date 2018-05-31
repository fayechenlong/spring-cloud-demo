package com.beeplay.core.quartz;

import java.lang.annotation.*;

/**
 * 开机启动的方法
 * 普通方法还是定时任务
 * @author chenlongfei
 *
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Startup {
           int index() default 0;
           boolean task() default false;
           String taskName() default "";
           String cronExpression() default "";
           String description() default "";
}
