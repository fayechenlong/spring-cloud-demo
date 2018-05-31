package com.beeplay.core.quartz;

import java.lang.annotation.*;

/**
 * 开机启动的类
 * 普通方法还是定时任务
 * @author chenlongfei
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InitClass {

}
