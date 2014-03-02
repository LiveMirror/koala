package org.openkoala.businesslog.common;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.openkoala.businesslog.BusinessLogPropertiesConfigException;

/**
 * User: zjzhai
 * Date: 12/12/13
 * Time: 3:10 PM
 */
public class BusinessLogPropertiesConfig {

    private final static String BUSINESS_LOG_CONFIG_PROPERTIES_NAME = "koala-businesslog.properties";

    private final static String LOG_ENABLE = "kaola.businesslog.enable";


    private static BusinessLogPropertiesConfig ourInstance = new BusinessLogPropertiesConfig();

    private static PropertiesConfiguration configuration;

    public static BusinessLogPropertiesConfig getInstance() {
        try {
            configuration = new PropertiesConfiguration(BUSINESS_LOG_CONFIG_PROPERTIES_NAME);
        } catch (ConfigurationException e) {
            throw new BusinessLogPropertiesConfigException(e);
        }
        return ourInstance;
    }

    public boolean getLogEnableConfig() {
        return configuration.getBoolean(LOG_ENABLE, false);
    }

}
