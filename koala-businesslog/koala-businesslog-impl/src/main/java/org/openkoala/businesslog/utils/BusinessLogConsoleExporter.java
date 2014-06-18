package org.openkoala.businesslog.utils;

import org.openkoala.businesslog.BusinessLog;
import org.openkoala.businesslog.BusinessLogExporter;

/**
 * 日志导出器默认实现
 * User: zjzhai
 * Date: 3/5/14
 * Time: 10:05 AM
 */
public class BusinessLogConsoleExporter implements BusinessLogExporter {


    @Override
    public void export(BusinessLog businessLog) {
        System.out.println(businessLog);
    }


}
