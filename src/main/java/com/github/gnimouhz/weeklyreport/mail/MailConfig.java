package com.github.gnimouhz.weeklyreport.mail;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * mail配置
 *
 * @author liuzhuoming
 * @datetime 2019/2/18 15:21
 */
@Component
@Data
public class MailConfig {

    @Value("${gnimouhz.mail.from}")
    private String from;
    @Value("${gnimouhz.mail.to}")
    private String to;
    @Value("${gnimouhz.mail.cc}")
    private String cc;
}
