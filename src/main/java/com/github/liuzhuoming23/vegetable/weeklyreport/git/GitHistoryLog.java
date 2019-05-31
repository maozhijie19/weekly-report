package com.github.liuzhuoming23.vegetable.weeklyreport.git;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * weekly-report：git日志历史
 *
 * @author liuzhuoming
 */
@Data
@AllArgsConstructor
public class GitHistoryLog implements Serializable {

    private static final long serialVersionUID = 8996913894835118412L;
    /**
     * 作者
     */
    private String author;
    /**
     * 日期
     */
    private Date date;
    /**
     * 提交信息
     */
    private String message;
}
