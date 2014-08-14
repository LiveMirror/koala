package org.openkoala.security.facade;

/**
 * Created by yyang on 14-8-14.
 */
public class InvokeResult<T> {

    private T data;

    private String errorMessage;

    private boolean hasErrors;

    public static <T> InvokeResult<T> success(T data) {
        InvokeResult<T> result = new InvokeResult<T>();
        result.data = data;
        result.hasErrors = false;
        return result;
    }

    public static <T> InvokeResult<T> success() {
        InvokeResult<T> result = new InvokeResult<T>();
        result.hasErrors = false;
        return result;
    }

    public static <T> InvokeResult<T> failure(String message) {
        InvokeResult<T> result = new InvokeResult<T>();
        result.hasErrors = true;
        result.errorMessage = message;
        return result;
    }

    public T getData() {
        return data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isHasErrors() {
        return hasErrors;
    }

    public boolean isSuccess() {
        return !hasErrors;
    }
}
