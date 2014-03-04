package org.openkoala.businesslog.common;

import org.openkoala.businesslog.BusinessLogContextQueryExecutor;
import org.openkoala.businesslog.BusinessLogEngine;
import org.openkoala.businesslog.BusinessLogExporter;
import org.openkoala.businesslog.BusinessLogRender;
import org.openkoala.businesslog.config.BusinessLogConfigAdapter;
import org.openkoala.businesslog.utils.ThreadLocalBusinessLogContext;

import java.util.Collections;
import java.util.Map;

import static org.openkoala.businesslog.ContextKeyConstant.BUSINESS_METHOD;

/**
 * User: zjzhai
 * Date: 12/15/13
 * Time: 6:45 PM
 */
public class LogEngineThread implements Runnable {


    private Map<String, Object> context;

    private String joinPointSignature;

    private BusinessLogConfigAdapter configAdapter;

    private BusinessLogRender render;

    private BusinessLogExporter businessLogExporter;

    private BusinessLogContextQueryExecutor queryExecutor;

    public LogEngineThread(Map<String, Object> context, String joinPointSignature, BusinessLogConfigAdapter configAdapter, BusinessLogRender render, BusinessLogExporter businessLogExporter, BusinessLogContextQueryExecutor queryExecutor) {
        this.context = context;
        this.joinPointSignature = joinPointSignature;
        this.configAdapter = configAdapter;
        this.render = render;
        this.businessLogExporter = businessLogExporter;
        this.queryExecutor = queryExecutor;
    }

    @Override
    public void run() {
        ThreadLocalBusinessLogContext.put(BUSINESS_METHOD, joinPointSignature);
        BusinessLogEngine businessLogEngine = new BusinessLogEngine(configAdapter, render, queryExecutor);
        businessLogEngine.setInitContext(Collections.unmodifiableMap(context));
        businessLogEngine.exportLogBy(joinPointSignature, businessLogExporter);
    }


}
