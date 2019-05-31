package com.github.liuzhuoming23.vegetable.weeklyreport.schedule;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * schedule配置
 *
 * @author liuzhuoming
 */
@Component
@Data
public class ScheduleConfig {

    @Value("${vegetable.schedule.cron}")
    private String cron;
}
