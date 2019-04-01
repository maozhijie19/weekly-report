package com.github.gnimouhz.weeklyreport.controller;

import com.github.gnimouhz.weeklyreport.git.GitConfig;
import com.github.gnimouhz.weeklyreport.mail.MailConfig;
import com.github.gnimouhz.weeklyreport.schedule.ScheduleConfig;
import com.github.gnimouhz.weeklyreport.svn.SvnConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

/**
 * 首页controller
 *
 * @author liuzhuoming
 * @datetime 2019/2/19 16:05
 */
@Controller
public class IndexController {

    @Autowired
    private ScheduleConfig scheduleConfig;
    @Autowired
    private MailConfig mailConfig;
    @Autowired
    private SvnConfig svnConfig;
    @Autowired
    private GitConfig gitConfig;

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("svn", new HashMap<String, String>() {{
            put("url", svnConfig.getUrl());
            put("username", svnConfig.getUsername());
        }});
        model.addAttribute("git", new HashMap<String, String>() {{
            put("url", gitConfig.getUrl());
            put("username", gitConfig.getUsername());
        }});
        model.addAttribute("mail", new HashMap<String, String>() {{
            put("from", mailConfig.getFrom());
            put("to", mailConfig.getTo());
            put("cc", mailConfig.getCc());
        }});
        model.addAttribute("schedule", new HashMap<String, String>() {{
            put("cron", scheduleConfig.getCron());
        }});
        return "index";
    }
}
