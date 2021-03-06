package com.github.liuzhuoming23.vegetable.weeklyreport.schedule;

import com.github.liuzhuoming23.vegetable.weeklyreport.mail.TextMailSender;
import com.github.liuzhuoming23.vegetable.weeklyreport.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.tmatesoft.svn.core.SVNException;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.ParseException;

/**
 * 发送周报邮件任务
 *
 * @author liuzhuoming
 */
@Slf4j
@EnableScheduling
public class MailSendJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        try {
            TextMailSender textMailSender = (TextMailSender) SpringContextUtil
                .getBean("textMailSender");
            textMailSender.send();
        } catch (SVNException | MessagingException | ParseException | IOException | GitAPIException e) {
            log.error("发送周报失败|" + e.getMessage());
        }
    }
}

