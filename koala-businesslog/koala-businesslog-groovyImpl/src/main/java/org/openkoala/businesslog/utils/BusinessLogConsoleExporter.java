package org.openkoala.businesslog.utils;

import org.openkoala.businesslog.BusinessLog;
import org.openkoala.businesslog.BusinessLogExporter;

/**
 * just for test
 * User: zjzhai
 * Date: 3/6/14
 * Time: 11:10 AM
 */
public class BusinessLogConsoleExporter implements BusinessLogExporter {
    @Override
    public void export(BusinessLog businessLog) {
        System.out.println( businessLog.getCategory() + ":" +businessLog.getLog());
    }
}
