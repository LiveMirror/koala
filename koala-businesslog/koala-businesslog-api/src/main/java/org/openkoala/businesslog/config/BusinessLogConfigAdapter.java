package org.openkoala.businesslog.config;

/**
 * 业务日志配置适配器
 * User: zjzhai
 * Date: 12/4/13
 * Time: 10:30 AM
 */
public interface BusinessLogConfigAdapter {

    BusinessLogConfig findConfigBy(String businessOperation);
}
