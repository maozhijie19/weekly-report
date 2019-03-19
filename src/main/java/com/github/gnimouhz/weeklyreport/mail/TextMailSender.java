package com.github.gnimouhz.weeklyreport.mail;

import com.github.gnimouhz.weeklyreport.git.GitConfig;
import com.github.gnimouhz.weeklyreport.git.GitHistory;
import com.github.gnimouhz.weeklyreport.git.GitHistoryLog;
import com.github.gnimouhz.weeklyreport.svn.SvnConfig;
import com.github.gnimouhz.weeklyreport.svn.SvnHistory;
import com.github.gnimouhz.weeklyreport.svn.SvnHistoryLog;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.tmatesoft.svn.core.SVNException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 发送html邮件
 *
 * @author liuzhuoming
 * @datetime 2019/1/4 9:57
 */
@Component
@Slf4j
public class TextMailSender {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private JavaMailSender javaMailSender;
    @Autowired
    private MailConfig mailConfig;
    @Autowired
    private SvnConfig svnConfig;
    @Autowired
    private GitConfig gitConfig;
    @Autowired
    private SvnHistory svnHistory;
    @Autowired
    private GitHistory gitHistory;

    private static final String TRUE = "true";

    public void send()
        throws SVNException, MessagingException, ParseException, GitAPIException, IOException {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy.MM.dd");

        log.error(mailConfig.toString());

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(mailConfig.getFrom());
        helper.setTo(mailConfig.getTo());
        if (mailConfig.getCc() != null && mailConfig.getCc().length() != 0) {
            helper.setCc(mailConfig.getCc().split(","));
        }
        helper.setSubject("刘卓明-"
            + sdfDate.format(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 7))
            + "~"
            + sdfDate.format(new Date())
            + "-周报");
        StringBuilder stringBuilder = new StringBuilder();
        //svn日志
        if (TRUE.equalsIgnoreCase(svnConfig.getEnable())) {
            log.error(svnConfig.toString());
            String[] urls = svnConfig.getUrl().split(",");
            String[] projectNames = svnConfig.getProjectname().split(",");
            for (int i = 0; i < urls.length; i++) {
                String url = urls[i];
                String projectName = "";
                try {
                    projectName = projectNames[i];
                } catch (Exception ignored) {

                }
                stringBuilder
                    .append("<h1>")
                    .append(projectName)
                    .append("（")
                    .append(url.substring(url.lastIndexOf('/') + 1))
                    .append("）")
                    .append("</h1>");
                for (SvnHistoryLog svnHistoryLog : svnHistory
                    .sortedAndMergeHistory(url, svnConfig.getUsername(), svnConfig.getPassword())) {
                    stringBuilder
                        .append("<b>")
                        .append(sdfDate.format(svnHistoryLog.getDate()))
                        .append("</b><br/>")
                        .append(svnHistoryLog.getMessage().replace("\\n", ""))
                        .append("<br/>");
                }
            }
        }
        //git日志
        if (TRUE.equalsIgnoreCase(gitConfig.getEnable())) {
            log.error(gitConfig.toString());
            String[] urls = gitConfig.getUrl().split(",");
            String[] paths = gitConfig.getPath().split(",");
            String[] projectNames = gitConfig.getProjectname().split(",");
            for (int i = 0; i < paths.length; i++) {
                String path = paths[i];
                String url = urls[i];
                String projectName = "";
                try {
                    projectName = projectNames[i];
                } catch (Exception ignored) {

                }
                stringBuilder
                    .append("<h1>")
                    .append(projectName)
                    .append("（")
                    .append(path.substring(path.lastIndexOf('\\') + 1))
                    .append("）")
                    .append("</h1>");
                for (GitHistoryLog gitHistoryLog : gitHistory
                    .sortedAndMergeHistory(url, path, gitConfig.getUsername())) {
                    stringBuilder
                        .append("<b>")
                        .append(sdfDate.format(gitHistoryLog.getDate()))
                        .append("</b><br/>")
                        .append(gitHistoryLog.getMessage().replace("\\n", ""))
                        .append("<br/>");
                }
            }
        }

        helper.setText(stringBuilder.toString(), true);
        javaMailSender.send(message);
    }
}
