package com.github.gnimouhz.weeklyreport.schedule;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * schedule配置
 *
 * @author liuzhuoming
 * @datetime 2019/2/18 15:52
 */
@Component
@Data
public class ScheduleConfig {

    @Value("${gnimouhz.schedule.cron}")
    private String cron;
}
