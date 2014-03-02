package org.openkoala.businesslog.common;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.openkoala.businesslog.FreemarkerProcessorException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * User: zjzhai
 * Date: 12/7/13
 * Time: 8:28 PM
 */
public class FreemarkerProcessor {

    private static Configuration configuration = new Configuration();

    public static String process(String utf8Template, Map<String, Object> aContext) {
        return process(utf8Template, aContext, "UTF8");

    }


    /**
     * TODO 性能待重构
     * 使用 aContext 填充 template模板
     *
     * @param template
     * @param aContext
     * @param templateEncoding 模板编码
     * @return
     */
    public static String process(String template, Map<String, Object> aContext, String templateEncoding) {
        if (null == template || "".equals(template.trim())) {
            return "";
        }
        StringTemplateLoader stringLoader = new StringTemplateLoader();
        stringLoader.putTemplate("template", template);
        configuration.setTemplateLoader(stringLoader);
        StringWriter out = new StringWriter(512);
        try {
            Template freemarkerTemplate = configuration.getTemplate("template", templateEncoding);
            freemarkerTemplate.process(aContext, out);
        } catch (IOException e) {
            throw new FreemarkerProcessorException(e);
        } catch (TemplateException e) {
            throw new FreemarkerProcessorException(e);
        } finally {
            configuration.clearSharedVariables();
            configuration.clearTemplateCache();
        }
        return out.toString();
    }

}
