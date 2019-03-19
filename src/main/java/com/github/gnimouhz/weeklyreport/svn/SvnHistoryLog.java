package com.github.gnimouhz.weeklyreport.svn;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * svn提交历史日志
 *
 * @author liuzhuoming
 * @datetime 2019/2/18 14:27
 */
@Data
@AllArgsConstructor
public class SvnHistoryLog implements Serializable {

    private static final long serialVersionUID = 7716712263391814846L;
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
