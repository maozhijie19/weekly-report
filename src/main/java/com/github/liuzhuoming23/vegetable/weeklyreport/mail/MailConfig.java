package com.github.liuzhuoming23.vegetable.weeklyreport.mail;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * mail配置
 *
 * @author liuzhuoming
 */
@Component
@Data
public class MailConfig {

    @Value("${vegetable.mail.from}")
    private String from;
    @Value("${vegetable.mail.to}")
    private String to;
    @Value("${vegetable.mail.cc}")
    private String cc;
}
