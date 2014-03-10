package org.openkoala.businesslog;

/**
 * User: zjzhai
 * Date: 3/2/14
 * Time: 9:27 PM
 */
public class BusinessLogBaseException extends RuntimeException {
    public BusinessLogBaseException() {
    }

    public BusinessLogBaseException(String message) {
        super(message);
    }

    public BusinessLogBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessLogBaseException(Throwable cause) {
        super(cause);
    }
}
