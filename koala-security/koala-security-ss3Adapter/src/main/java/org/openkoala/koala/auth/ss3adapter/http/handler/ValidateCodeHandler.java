package org.openkoala.koala.auth.ss3adapter.http.handler;

import com.octo.captcha.module.servlet.image.SimpleImageCaptchaServlet;
import org.openkoala.koala.auth.ss3adapter.BadValidateCodeException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 验证码处理器
 *
 * @author zhuyuanbiao
 * @date 2014年1月7日 上午11:11:10
 */
public class ValidateCodeHandler implements HttpHandler {

    private boolean allowEmptyValidateCode = false;

    private String sessionvalidateCodeField = DEFAULT_SESSION_VALIDATE_CODE_FIELD;

    public static final String SPRING_SECURITY_LAST_USERNAME_KEY = "SPRING_SECURITY_LAST_USERNAME";

    // session中保存的验证码
    public static final String DEFAULT_SESSION_VALIDATE_CODE_FIELD = "rand";

    // 输入的验证码
    public static final String DEFAULT_VALIDATE_CODE_PARAMETER = "code";

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) {
        checkValidateCode(request);
    }

    protected String obtainSessionValidateCode(HttpServletRequest request) {
        return getValidateCodeField(request) == null ? "" : getValidateCodeField(request).toString();
    }

    public String getValidateCodeName() {
        return sessionvalidateCodeField;
    }

    public void setValidateCodeName(String validateCodeName) {
        this.sessionvalidateCodeField = validateCodeName;
    }

    public boolean isAllowEmptyValidateCode() {
        return allowEmptyValidateCode;
    }

    public void setAllowEmptyValidateCode(boolean allowEmptyValidateCode) {
        this.allowEmptyValidateCode = allowEmptyValidateCode;
    }

    /**
     * 比较session中的验证码和用户输入的验证码是否相等
     *
     * @param request
     */
    private void checkValidateCode(HttpServletRequest request) {
        if (!isValid(request)) {
            throw new BadValidateCodeException("Bad validate code.");
        }
    }

    private boolean isValid(HttpServletRequest request) {
        return SimpleImageCaptchaServlet.validateResponse(request, request.getParameter("jcaptcha"));
    }

    private Object getValidateCodeField(HttpServletRequest request) {
        return request.getSession().getAttribute(sessionvalidateCodeField);
    }

}
