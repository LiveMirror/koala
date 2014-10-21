package org.openkoala.businesslog;

/**
 * 日志导出器，通过实现此接口来导出你的日志。比如希望将日志存入到数据库中
 * User: zjzhai
 * Date: 12/1/13
 * Time: 9:36 PM
 */
public interface BusinessLogExporter {
    void export(BusinessLog businessLog);
}
