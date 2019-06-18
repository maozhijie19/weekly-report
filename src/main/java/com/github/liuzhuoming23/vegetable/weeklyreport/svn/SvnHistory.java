package com.github.liuzhuoming23.vegetable.weeklyreport.svn;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * svn历史
 *
 * @author liuzhuoming
 */
@Component
@Slf4j
public class SvnHistory {

    /**
     * 获取全部svn历史日志
     *
     * @param url svn url
     * @return 全部svn历史日志
     */
    private List<SvnHistoryLog> allHistory(String url, String username, String password)
        throws SVNException {
        DAVRepositoryFactory.setup();

        long startRevision = 0;
        long endRevision = -1;

        SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
        ISVNAuthenticationManager authManager = SVNWCUtil
            .createDefaultAuthenticationManager(username, password);
        repository.setAuthenticationManager(authManager);

        Collection logEntries = repository
            .log(new String[]{""}, null, startRevision, endRevision, true, true);

        List<SvnHistoryLog> list = new ArrayList<>();
        for (Object object : logEntries) {
            SVNLogEntry logEntry = (SVNLogEntry) object;
            SvnHistoryLog svnHistoryLog = new SvnHistoryLog(logEntry.getAuthor(),
                logEntry.getDate(), logEntry.getMessage());
            list.add(svnHistoryLog);
        }
        return list;
    }

    /**
     * 获取当前svn用户历史日志（七天内）
     *
     * @param url svn url
     * @param username svn username
     * @param password svn password
     * @return 当前svn用户历史日志
     */
    private List<SvnHistoryLog> currentUserHistory(String url, String username, String password)
        throws SVNException {
        List<SvnHistoryLog> list = allHistory(url, username, password);
        list = list.stream()
            //时间先后排序
            .sorted((o1, o2) -> {
                long t1 = o1.getDate().getTime();
                long t2 = o2.getDate().getTime();
                if (t1 > t2) {
                    return 1;
                } else if (t1 < t2) {
                    return -1;
                }
                return 0;
            })
            //过滤上周六到当前时间内日志
            .filter(o -> {
                long date = o.getDate().getTime();
                LocalDateTime lastSaturday
                    = LocalDateTime.now().minusWeeks(1).with(DayOfWeek.FRIDAY)
                    .withHour(18).withMinute(30);
                long ts = lastSaturday.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
                return date > ts;
            })
            //过滤当前用户日志
            .filter(o -> username.equals(o.getAuthor()))
            .collect(Collectors.toList());
        return list;
    }

    /**
     * 获取当前svn用户按照日期排序并合并后的历史日志（七天内）
     *
     * @param url svn url
     * @param username svn username
     * @param password svn password
     * @return 当前svn用户历史日志
     */
    public List<SvnHistoryLog> sortedAndMergeHistory(String url, String username, String password)
        throws ParseException, SVNException {
        SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy/MM/dd");
        List<SvnHistoryLog> list = currentUserHistory(url, username, password);
        Set<Date> set = new HashSet<>();
        for (SvnHistoryLog log : list) {
            set.add(sdfDateTime.parse(sdfDateTime.format(log.getDate())));
            log.setDate(sdfDateTime.parse(sdfDateTime.format(log.getDate())));
        }
        List<SvnHistoryLog> logs = new ArrayList<>();
        for (Date date : set) {
            SvnHistoryLog log = new SvnHistoryLog("", date, "");
            logs.add(log);
        }
        for (SvnHistoryLog log : list) {
            for (SvnHistoryLog log1 : logs) {
                if (log.getDate().equals(log1.getDate())) {
                    String logStr = log1.getMessage() + log.getMessage().replaceAll("\\n", "");
                    if (!logStr.endsWith("；")) {
                        logStr += "；";
                    }
                    log1.setMessage(logStr);
                }
            }
        }
        for (SvnHistoryLog log : logs) {
            log.setMessage(log.getMessage().substring(0, log.getMessage().length() - 1) + "。");
        }
        logs = logs.stream()
            //时间先后排序
            .sorted((o1, o2) -> {
                long t1 = o1.getDate().getTime();
                long t2 = o2.getDate().getTime();
                if (t1 > t2) {
                    return 1;
                } else if (t1 < t2) {
                    return -1;
                }
                return 0;
            })
            //格式化日志（换行）
            .peek(x -> x.setMessage(x.getMessage().replace("；", "；<br/>")))
            .collect(Collectors.toList());
        return logs;
    }
}
