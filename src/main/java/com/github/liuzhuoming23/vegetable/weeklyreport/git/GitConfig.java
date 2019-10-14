package com.github.liuzhuoming23.vegetable.weeklyreport.git;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * weekly-report：git相关配置项
 *
 * @author liuzhuoming
 */
@Component
@Data
public class GitConfig {

    @Value("${vegetable.git.enable:false}")
    private Boolean enable;
    @Value("${vegetable.git.path}")
    private String path;
    @Value("${vegetable.git.url}")
    private String url;
    @Value("${vegetable.git.username}")
    private String username;
    @Value("${vegetable.git.password}")
    private String password;
    @Value("${vegetable.git.projectname}")
    private String projectname;
    @Value("${vegetable.git.dayOfWeek:5}")
    private Integer dayOfWeek;
    @Value("${vegetable.git.hours:18}")
    private Integer hours;
    @Value("${vegetable.git.minutes:00}")
    private Integer minutes;
    @Value("${vegetable.git.branchName:''}")
    private String branchName;
    @Value("${vegetable.git.authorName:''}")
    private String authorName;
}
