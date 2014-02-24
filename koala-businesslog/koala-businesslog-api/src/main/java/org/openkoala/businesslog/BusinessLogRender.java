package org.openkoala.businesslog;

import java.util.Map;

/**
 * 日志渲染器。比如你的日志模版是使用Freemarker写的，那么，你就需要实现一个Freemarker的渲染，依此类推velocity渲染器的实现。
 * User: zjzhai
 * Date: 11/29/13
 * Time: 4:20 PM
 */
public interface BusinessLogRender {


    public String render(Map<String, Object> context, String template);

}
