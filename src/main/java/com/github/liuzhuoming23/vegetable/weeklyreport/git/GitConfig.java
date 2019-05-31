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

    @Value("${vegetable.git.enable}")
    private String enable;
    @Value("${vegetable.git.path}")
    private String path;
    @Value("${vegetable.git.url}")
    private String url;
    @Value("${vegetable.git.username}")
    private String username;
    @Value("${vegetable.git.projectname}")
    private String projectname;
}
