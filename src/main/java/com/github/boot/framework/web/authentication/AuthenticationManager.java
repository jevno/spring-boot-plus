package com.github.boot.framework.web.authentication;

import com.github.boot.framework.util.ConstUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * AuthenticationManager
 *
 * ID 用户ID类型
 * T 用户类型
 * @author chenjianhui
 * @create 2018/05/24
 **/
public abstract class AuthenticationManager<T> {

    /**
     * 应用名称
     */
    private String application = "";

    /**
     * Token 管理器
     */
    private TokenManager tokenManager = new JwsTokenManager();

    /**
     * 用户授权
     * @param userId
     * @return
     */
    public abstract Authentication<T> authenticate(Object userId);

    /**
     * 用户登录
     * @param authentication
     * @return
     */
    public Authentication<T> login(Authentication<T> authentication){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpSession session = request.getSession();
        HttpServletResponse response = requestAttributes.getResponse();
        session.setAttribute(ConstUtils.SESSION_USER_ID, authentication.getUserId());
        tokenManager.sendToken(tokenManager.createToken(authentication), response);
        return authentication;
    }

    /**
     * 获取授权用户信息
     * @return
     */
    public T getUser(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpSession session = request.getSession();
        Object userId = session.getAttribute(ConstUtils.SESSION_USER_ID);
        Authentication<T> authenticate = authenticate(userId);
        return authenticate.getUserInfo();
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public TokenManager getTokenManager() {
        return tokenManager;
    }

    public void setTokenManager(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

}