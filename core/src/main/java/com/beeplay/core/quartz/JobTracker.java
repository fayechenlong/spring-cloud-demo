package com.beeplay.core.quartz;

/**
 * @author  chenlongfei
 * @date 2016-11-11
 */
public abstract class JobTracker {
    public abstract void run(ScheduleJobModel scheduleJob);
}
