package com.github.liuzhuoming23.vegetable.weeklyreport.mail;

import lombok.Data;

/**
 * html mail实体类
 *
 * @author liuzhuoming
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
