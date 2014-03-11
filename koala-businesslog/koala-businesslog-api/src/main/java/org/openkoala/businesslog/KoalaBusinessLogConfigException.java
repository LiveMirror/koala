package org.openkoala.businesslog;

/**
 * User: zjzhai
 * Date: 3/4/14
 * Time: 5:11 PM
 */
public class KoalaBusinessLogConfigException extends RuntimeException {

    public KoalaBusinessLogConfigException() {
        super();
    }

    public KoalaBusinessLogConfigException(String message) {
        super(message);
    }

    public KoalaBusinessLogConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public KoalaBusinessLogConfigException(Throwable cause) {
        super(cause);
    }

    protected KoalaBusinessLogConfigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        //FIXME 编译出错
        //super(message, cause, enableSuppression, writableStackTrace);
    }
}
