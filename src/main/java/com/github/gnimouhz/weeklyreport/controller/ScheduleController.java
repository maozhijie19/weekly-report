package com.github.gnimouhz.weeklyreport.controller;

import com.github.gnimouhz.weeklyreport.schedule.QuartzSchedule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 调度任务controller
 *
 * @author liuzhuoming
 * @datetime 2019/2/19 18:11
 */
@RestController
@Slf4j
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private QuartzSchedule quartzSchedule;

    @RequestMapping("/start")
    public String start() {
        try {
            quartzSchedule.execute();
            return "调度器执行成功";
        } catch (Exception e) {
            log.error("调度器执行错误：" + e.getMessage());
            return "调度器执行错误：" + e.getMessage();
        }
    }
}
