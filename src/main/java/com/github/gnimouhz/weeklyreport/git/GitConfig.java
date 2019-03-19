package com.github.gnimouhz.weeklyreport.git;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * weekly-report：git相关配置项
 *
 * @author gnimouhzuil
 * @date 2019/3/19 10:34
 */
@Component
@Data
public class GitConfig {

    @Value("${gnimouhz.git.enable}")
    private String enable;
    @Value("${gnimouhz.git.path}")
    private String path;
    @Value("${gnimouhz.git.url}")
    private String url;
    @Value("${gnimouhz.git.username}")
    private String username;
    @Value("${gnimouhz.git.projectname}")
    private String projectname;
}
