package com.beeplay.core.quartz;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


/**
 * @Description: 有状态任务执行
 * @author chenlongfei
 */
@DisallowConcurrentExecution
public class QuartzJobFactoryDisallowConcurrentExecution implements Job {
	public final Logger log = Logger.getLogger(this.getClass());

	public void execute(JobExecutionContext context) throws JobExecutionException {
		ScheduleJobModel scheduleJob = (ScheduleJobModel) context.getMergedJobDataMap().get(BaseContext.SCHEDULE_JOB_CONTEXT);
		TaskUtils.invokMethod(scheduleJob);
	}
}
