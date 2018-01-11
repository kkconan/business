package com.money.game.business.controller;

import com.money.game.basic.component.exception.GHException;
import com.money.game.basic.component.ext.web.PersistentCookieHttpSessionStrategy;
import com.money.game.basic.component.ext.web.Response;
import com.money.game.basic.config.TerminalConfig;
import com.money.game.core.util.StrRedisUtil;
import com.money.game.core.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.MappedSuperclass;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

@MappedSuperclass
public class BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    public static final String UID = "UID";// 用户ID
    public static final String UNAME = "UNAME";// 用户姓名
    private static final Integer expireTime = 24 * 60 * 60;//token失效时间

    @Autowired
    protected HttpSession session;

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    @Autowired
    private TerminalConfig terminalConfig;

    @Autowired
    private PersistentCookieHttpSessionStrategy persistentCookieHttpSessionStrategy;

    @Autowired
    private RedisTemplate<String, String> redis;

    @Value("${error.print.strategy:logger}")
    private String onError;

    @ExceptionHandler
    public
    @ResponseBody
    ResponseEntity<Response> onError(HttpServletRequest request, Exception exception) {

        this.errorLogHandle(exception);

        Response response = new Response().error(exception);
        Enumeration<String> enums = this.request.getParameterNames();
        while (enums.hasMoreElements()) {
            String name = enums.nextElement();
            response.with(name, this.request.getParameter(name));
        }

        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    private void errorLogHandle(Exception e) {
        if ("logger".equals(this.onError)) {
            if (e instanceof GHException) {
                logger.info(e.getMessage());
            } else {
                logger.info(e.getMessage(), e);
            }
        } else {
            e.printStackTrace();
        }
    }

    /**
     * 用户是否已经登录
     *
     * @return 登录id
     */
    protected String isLogin() {
        if (session.getAttribute(UID) != null) {
            String userOid = session.getAttribute(UID).toString();
            logger.debug("isLogin useroid {}, and sessionid is {}.", userOid, session.getId());
            if (terminalConfig.isUserLocked(userOid)) {
                throw new GHException(15002, "当前用户已锁定");
            }
            return userOid;
        } else {
            return null;
        }
    }

    protected void setLoginUser(String userOid, String... terminal) {
        if (terminal != null && terminal.length > 0) {
            persistentCookieHttpSessionStrategy.onNewSessionTwo(session.getId(), request, response, terminal[0]);
        }
        session.setAttribute(UID, userOid);
        terminalConfig.handleMultiLogin(session.getId(), userOid, terminal);
        logger.info("useroid {} login, and sessionid is {}.", userOid, session.getId());
    }

    protected void setLoginUser(String userOid, String userName, String... terminal) {
        if (terminal != null && terminal.length > 0) {
            persistentCookieHttpSessionStrategy.onNewSessionTwo(session.getId(), request, response, terminal[0]);
        }
        session.setAttribute(UID, userOid);
        session.setAttribute(UNAME, userName);
        terminalConfig.handleMultiLogin(session.getId(), userOid, terminal);
        logger.info("useroid {},userName {} login, and sessionid is {}.", userOid, userName, session.getId());
    }

    protected String getLoginUser() {
        if (session.getAttribute(UID) == null) {
            throw new GHException(10002, "当前用户未登录或会话已超时");
        }
        String userOid = session.getAttribute(UID).toString();
        if (terminalConfig.isUserLocked(userOid)) {
            throw new GHException(15002, "当前用户已锁定");
        }
        logger.debug("get login useroid {}, and sessionid is {}.", userOid, session.getId());
        return userOid;
    }


    /**
     * 检查用户是否通过一次性token完成登录
     */
    protected String getLoginUserByToken(String token) {
        String userOid = null;
        if (!StringUtil.isEmpty(token)) {
            String value = StrRedisUtil.get(redis, token);
            logger.info("token={},proValue={}", token, value);
            //token值是否存在
            if (StringUtil.isEmpty(value)) {
                value = "1";
                StrRedisUtil.setEx(redis, token,expireTime, value);
                throw new GHException(10002, "当前用户未登录或会话已超时");
            } else {
                String[] values = value.split("_");
                Integer userTime = Integer.valueOf(values[0]);
                //values中存在userOid
                if(values.length > 1) {
                    userOid = values[1];
                }
                //token未被使用且userOid不为空
                if (userTime == 0 && StringUtils.isNotEmpty(userOid)) {
                    this.setLoginUser(userOid);
                    value = ++userTime + "_" + userOid;
                    StrRedisUtil.setEx(redis, token, expireTime,value);
                } else {
                    if (session.getAttribute(UID) == null) {
                        throw new GHException(10002, "当前用户未登录或会话已超时");
                    }
                    userOid = session.getAttribute(UID).toString();
                }
            }
        } else {
            if (session.getAttribute(UID) == null) {
                throw new GHException(10002, "当前用户未登录或会话已超时");
            }
            userOid = session.getAttribute(UID).toString();

        }
        if (terminalConfig.isUserLocked(userOid)) {
            throw new GHException(15002, "当前用户已锁定");
        }
        logger.debug("get login useroid {}, and sessionid is {}.", userOid, session.getId());
        return userOid;
    }

    protected String getLoginUserName() {
        if (session.getAttribute(UNAME) == null) {
            throw new GHException(10002, "当前用户未登录或会话已超时");
        }
        String userName = session.getAttribute(UNAME).toString();
        logger.debug("get login userName {},and sessionid is {}.", userName, session.getId());
        return userName;
    }

    protected void setLogoutUser() {
        String userOid = "";
        if (session.getAttribute(UID) != null) {
            userOid = (String) session.getAttribute(UID);
            session.removeAttribute(UID);
            session.removeAttribute(UNAME);
            this.terminalConfig.removeLoginUser(userOid);
        }
        logger.info("useroid {} logout, and sessionid is {}.", userOid, session.getId());
        session.invalidate();
    }

    protected String getToken() {
        String uuid = StringUtil.uuid();
        if (session.getAttribute(UID) != null) {
            String userId = session.getAttribute(UID).toString();
            //表示token未被使用过
            String value = "0" + "_" + userId;
            StrRedisUtil.setEx(redis, uuid, expireTime, value);
            return uuid;
        }
        return uuid;
    }
}
