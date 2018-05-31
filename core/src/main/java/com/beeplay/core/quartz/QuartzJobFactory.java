package com.beeplay.core.quartz;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @Description: 计划任务执行处 无状态
 * @author chenlongfei
 */
public class QuartzJobFactory implements Job {
	public final Logger log = Logger.getLogger(this.getClass());

	public void execute(JobExecutionContext context) throws JobExecutionException {
		ScheduleJobModel scheduleJob = (ScheduleJobModel) context.getMergedJobDataMap().get(BaseContext.SCHEDULE_JOB_CONTEXT);
		TaskUtils.invokMethod(scheduleJob);
	}
}
