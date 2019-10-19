package com.github.liuzhuoming23.vegetable.weeklyreport.svn;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * svn配置
 *
 * @author liuzhuoming
 */
@Component
@Data
public class SvnConfig {

    @Value("${vegetable.svn.enable:false}")
    private Boolean enable;
    @Value("${vegetable.svn.url}")
    private String url;
    @Value("${vegetable.svn.username}")
    private String username;
    @Value("${vegetable.svn.password}")
    private String password;
    @Value("${vegetable.svn.projectname}")
    private String projectname;
    @Value("${vegetable.svn.dayOfWeek:5}")
    private Integer dayOfWeek;
    @Value("${vegetable.svn.hours:18}")
    private Integer hours;
    @Value("${vegetable.svn.minutes:00}")
    private Integer minutes;
}
