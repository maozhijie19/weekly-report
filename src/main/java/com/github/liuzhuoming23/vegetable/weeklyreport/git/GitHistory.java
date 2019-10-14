package com.github.liuzhuoming23.vegetable.weeklyreport.git;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * weekly-report：git历史
 *
 * @author liuzhuoming
 */
@Component
@Slf4j
public class GitHistory {

    @Autowired
    private GitConfig gitConfig;


    /**
     * 从gitclone或者pull项目
     *
     * @param path 文件地址
     */
    private void cloneOrPullFromGit(String url, String path) throws GitAPIException, IOException {
        CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(
            gitConfig.getUsername(), gitConfig.getPassword());
        File dir = new File(path);
        if (dir.exists() && dir.isDirectory() && !new File(path + File.separator + ".git")
            .exists()) {
            Git.cloneRepository()
                .setURI(url)
                .setCredentialsProvider(credentialsProvider)
                .setDirectory(new File(path))
                .setCloneAllBranches(true)
                .call();
            if (gitConfig.getBranchName() != null && !"".equals(gitConfig.getBranchName())) {
                checkoutBranch(path, gitConfig.getBranchName());
            }
        } else {
            if (!dir.exists()) {
                //noinspection ResultOfMethodCallIgnored
                dir.mkdir();
            } else {
                if (dir.isFile()) {
                    throw new RuntimeException(path + "存在同名文件");
                }
            }
            Git.open(new File(path))
                .pull()
                .setCredentialsProvider(credentialsProvider)
                .call();
        }
    }

    public void checkoutBranch(String path, String branchName) {
        CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(
            gitConfig.getUsername(), gitConfig.getPassword());
        String projectURL = path + "\\.git";

        try (Git git = Git.open(new File(projectURL))) {
            git.checkout().setCreateBranch(true).setName(branchName).call();
            git.pull().setCredentialsProvider(credentialsProvider).call();
            System.out.println("切换分支成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("切换分支失败");
        }
    }

    /**
     * 获取全部git历史日志
     *
     * @param url  git url
     * @param path git项目路径
     */
    private List<GitHistoryLog> allHistory(String url, String path)
        throws GitAPIException, IOException {
        cloneOrPullFromGit(url, path);
        Git git = Git.open(new File(path));
        Iterable<RevCommit> iterable = git.log().call();
        List<GitHistoryLog> list = new ArrayList<>();
        for (RevCommit revCommit : iterable) {
            PersonIdent personIdent = revCommit.getCommitterIdent();
            GitHistoryLog gitHistoryLog = new GitHistoryLog(personIdent.getName(),
                new Date(1000L * revCommit.getCommitTime()),
                revCommit.getFullMessage());
            list.add(gitHistoryLog);
        }
        return list;
    }

    /**
     * 获取当前git用户历史日志（七天内）
     *
     * @param url  git url
     * @param path git path
     * @return 当前git用户历史日志
     */
    private List<GitHistoryLog> currentUserHistory(String url, String path)
        throws GitAPIException, IOException {
        List<GitHistoryLog> list = allHistory(url, path);
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
            //过滤上周五下午六点半到当前时间内日志
            .filter(o -> {
                long date = o.getDate().getTime();
                LocalDateTime lastSaturday
                    = LocalDateTime.now().minusWeeks(1).with(DayOfWeek.of(gitConfig.getDayOfWeek()))
                    .withHour(gitConfig.getHours()).withMinute(gitConfig.getMinutes());
                long ts = lastSaturday.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
                return date > ts;
            })
            //过滤当前用户日志
            .filter(o -> gitConfig.getAuthorName().equals(o.getAuthor()))
            //过滤合并代码日志
            .filter(o -> !o.getMessage().contains("Merge remote-tracking"))
            .collect(Collectors.toList());
        return list;
    }


    /**
     * 获取当前git用户按照日期排序并合并后的历史日志（七天内）
     *
     * @param url  git url
     * @param path git path
     * @return 当前git用户历史日志
     */
    public List<GitHistoryLog> sortedAndMergeHistory(String url, String path)
        throws ParseException, GitAPIException, IOException {
        SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy/MM/dd");
        List<GitHistoryLog> list = currentUserHistory(url, path);
        Set<Date> set = new HashSet<>();
        for (GitHistoryLog log : list) {
            set.add(sdfDateTime.parse(sdfDateTime.format(log.getDate())));
            log.setDate(sdfDateTime.parse(sdfDateTime.format(log.getDate())));
        }
        List<GitHistoryLog> logs = new ArrayList<>();
        for (Date date : set) {
            GitHistoryLog log = new GitHistoryLog("", date, "");
            logs.add(log);
        }
        for (GitHistoryLog log : list) {
            for (GitHistoryLog log1 : logs) {
                if (log.getDate().equals(log1.getDate())) {
                    String logStr = log1.getMessage() + log.getMessage().replaceAll("\\n", "");
                    if (!logStr.endsWith("；")) {
                        logStr += "；";
                    }
                    log1.setMessage(logStr);
                }
            }
        }
//        for (GitHistoryLog log : logs) {
//            log.setMessage(log.getMessage().substring(0, log.getMessage().length() - 1) + "。");
//        }
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
            .peek(x -> x.setMessage(x.getMessage().replace("；", "<br/>")))
            .collect(Collectors.toList());
        return logs;
    }
}
