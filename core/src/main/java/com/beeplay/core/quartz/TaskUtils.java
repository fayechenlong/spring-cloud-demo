package com.beeplay.core.quartz;


import com.beeplay.core.log.LogExceptionStackTrace;
import com.beeplay.core.utils.StringUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;


/**
 * @author chenlongfei
 */
public class TaskUtils {
	public final static Logger log = Logger.getLogger(TaskUtils.class);

	/**
	 * 通过反射调用scheduleJob中定义的方法
	 * @param scheduleJob
	 */
	public static boolean invokMethod(ScheduleJobModel scheduleJob) {
		Object object = null;
		Class clazz = null;
		if (StringUtils.isNotBlank(scheduleJob.getSpringId())) {
			//object = SpringUtil.getBean(scheduleJob.getSpringId());
		} else if (StringUtils.isNotBlank(scheduleJob.getBeanClass())) {
			try {
				clazz = Class.forName(scheduleJob.getBeanClass());
				object = clazz.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
				scheduleJob.setFailReason(LogExceptionStackTrace.erroStackTrace(e).toString());
				scheduleJob.setLastTime(new Date());
				return false;
			}

		}
		if (object == null) {
			scheduleJob.setFailReason("未启动成功，请检查BeanClass是否配置正确！！！");
			scheduleJob.setLastTime(new Date());
			log.error("任务名称 = [" + scheduleJob.getJobName() + "]---------------未启动成功，请检查是否配置正确！！！");
			return false;
		}
		clazz = object.getClass();
		Method method = null;
		try {
			method = clazz.getDeclaredMethod(scheduleJob.getMethodName());
		} catch (NoSuchMethodException e) {
			scheduleJob.setFailReason("未启动成功，方法名设置错误！！！");
			scheduleJob.setLastTime(new Date());
			log.error("任务名称 = [" + scheduleJob.getJobName() + "]---------------未启动成功，方法名设置错误！！！");
			return false;
		} catch (SecurityException e) {
			scheduleJob.setFailReason(e.getMessage());
			e.printStackTrace();
			return false;
		}
		if (method != null) {
			try {
				method.invoke(object);
				scheduleJob.setLastTime(new Date());
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				scheduleJob.setFailReason(LogExceptionStackTrace.erroStackTrace(e).toString());
				scheduleJob.setLastTime(new Date());
				log.error("任务名称 = [" + scheduleJob.getJobName() + "]--------------执行失败！！！");
				return false;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				scheduleJob.setFailReason(LogExceptionStackTrace.erroStackTrace(e).toString());
				scheduleJob.setLastTime(new Date());
				log.error("任务名称 = [" + scheduleJob.getJobName() + "]---------------执行失败！！！");
				return false;
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				scheduleJob.setFailReason(LogExceptionStackTrace.erroStackTrace(e).toString());
				scheduleJob.setLastTime(new Date());
				log.error("任务名称 = [" + scheduleJob.getJobName() + "]---------------执行失败！！！");
				return false;
			} catch (Exception e) {
				e.printStackTrace();
				scheduleJob.setFailReason(LogExceptionStackTrace.erroStackTrace(e).toString());
				scheduleJob.setLastTime(new Date());
				log.error("任务名称 = [" + scheduleJob.getJobName() + "]---------------执行失败！！！");
				return false;
			}
		}
		return true;
	}

}
