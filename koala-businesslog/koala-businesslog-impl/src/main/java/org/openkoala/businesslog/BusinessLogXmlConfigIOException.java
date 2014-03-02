package org.openkoala.businesslog;

/**
 * XML配置文件异常
 * User: zjzhai
 * Date: 12/13/13
 * Time: 9:14 AM
 */
public class BusinessLogXmlConfigIOException extends BusinessLogBaseException {

    public BusinessLogXmlConfigIOException() {
    }

    public BusinessLogXmlConfigIOException(String message) {
        super(message);
    }

    public BusinessLogXmlConfigIOException(Throwable cause) {
        super(cause);
    }

    public BusinessLogXmlConfigIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
