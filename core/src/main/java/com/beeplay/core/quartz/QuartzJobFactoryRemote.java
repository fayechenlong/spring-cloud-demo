package com.beeplay.core.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @Description: 计划任务执行处 无状态
 * @author chenlongfei
 */
public class QuartzJobFactoryRemote implements Job {

	public void execute(JobExecutionContext context) throws JobExecutionException {
		ScheduleJobModel scheduleJob = (ScheduleJobModel) context.getMergedJobDataMap().get(BaseContext.SCHEDULE_JOB_CONTEXT);
		/**
		 * 调用远程接口
		 */
		scheduleJob.getJobTracker().run(scheduleJob);
	}
}
