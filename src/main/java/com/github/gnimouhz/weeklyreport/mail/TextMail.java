package com.github.gnimouhz.weeklyreport.mail;

import lombok.Data;

/**
 * html mail实体类
 *
 * @author liuzhuoming
 * @datetime 2019/1/4 10:23
 */
@Data
public class TextMail {

    /**
     * 邮件主题
     */
    private String subject;
    /**
     * 邮件正文
     */
    private String text;
}
