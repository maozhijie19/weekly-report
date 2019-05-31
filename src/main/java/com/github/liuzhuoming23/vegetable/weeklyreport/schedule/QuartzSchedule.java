package com.github.liuzhuoming23.vegetable.weeklyreport.schedule;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * quartz调度
 *
 * @author liuzhuoming
 */
@Slf4j
@Component
public class QuartzSchedule {

    @Autowired
    private ScheduleConfig scheduleConfig;

    /**
     * 执行调度任务
     */
    public void execute() throws SchedulerException {
        // 获取调度器
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        // 注册任务和触发器
        registerJobAndTrigger(scheduler);
    }

    /**
     * 注册一个任务和触发器
     *
     * @param scheduler 调度器
     */
    private void registerJobAndTrigger(Scheduler scheduler) {
        JobDetail job = JobBuilder.newJob(MailSendJob.class)
            .withIdentity("mySimpleJob", "simpleGroup")
            .build();

        //简单调度器
//        Trigger trigger = org.quartz.TriggerBuilder.newTrigger()
//            .withIdentity("simpleTrigger", "simpleGroup")
//            .startNow()
//            .withSchedule(simpleSchedule()
//                .withIntervalInSeconds(100)
//                .repeatForever())
//            .build();

        //cron调度器
        CronTrigger trigger = TriggerBuilder.newTrigger().
            withIdentity("cronTrigger", "cronTrigger").
            withSchedule(CronScheduleBuilder.cronSchedule(scheduleConfig.getCron())).
            build();

        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            log.error("注册任务和触发器失败：" + e.getMessage());
        }
    }
}
