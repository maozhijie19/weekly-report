package com.github.gnimouhz.weeklyreport.svn;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * svn配置
 *
 * @author liuzhuoming
 * @datetime 2019/2/18 15:21
 */
@Component
@Data
public class SvnConfig {

    @Value("${gnimouhz.svn.enable}")
    private String enable;
    @Value("${gnimouhz.svn.url}")
    private String url;
    @Value("${gnimouhz.svn.username}")
    private String username;
    @Value("${gnimouhz.svn.password}")
    private String password;
    @Value("${gnimouhz.svn.projectname}")
    private String projectname;
}
