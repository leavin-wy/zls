package com.javaweb.system.filter;

import com.javaweb.common.utils.SpringUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

/**
 * 1.读取当前登录用户名，获取在缓存中的sessionId队列
 * 2.判断队列的长度，大于最大登录限制的时候，按踢出规则
 * 将之前的sessionId中的session域中存入kickout：true，并更新队列缓存
 * 3.判断当前登录的session域中的kickout如果为true，
 * 想将其做退出登录处理，返回给前端401
 */
public class KickoutSessionControlFilter extends AccessControlFilter {
    private static Logger logger = LoggerFactory.getLogger(KickoutSessionControlFilter.class);
    private String kickoutUrl; //踢出后到的地址
    private boolean kickoutAfter = false; //踢出之前登录的/之后登录的用户 默认踢出之前登录的用户
    private int maxSession = 1; //同一个帐号最大会话数 默认1

    private SessionManager sessionManager;
    private Cache<String, Deque<Serializable>> cache;

    public void setKickoutUrl(String kickoutUrl) {
        this.kickoutUrl = kickoutUrl;
    }

    public void setKickoutAfter(boolean kickoutAfter) {
        this.kickoutAfter = kickoutAfter;
    }

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    //设置Cache的key的前缀
    public void setCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("shiro_redis_cache:");
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
        Subject subject = getSubject(request, response);
        if (!subject.isAuthenticated() && !subject.isRemembered()) {
            //如果没有登录，直接进行之后的流程
            return true;
        }
        Session session = subject.getSession();
        if(subject.getSession().getAttribute("loginUserId") == null){
            return true;
        }
        String username = subject.getSession().getAttribute("loginUserId").toString();

        //对于admin不进行限制
        if("admin".equals(username)){
            return true;
        }

        Serializable sessionId = session.getId();

        //读取缓存   没有就存入
        Deque<Serializable> deque = cache.get(username);

        //如果此用户没有session队列，也就是还没有登录过，缓存中没有
        //就new一个空队列，不然deque对象为空，会报空指针
        if (deque == null) {
            deque = new LinkedList<Serializable>();
        }
        //如果队列里没有此sessionId，且用户没有被踢出；放入队列
        if (!deque.contains(sessionId) && !(Boolean) session.getAttribute("kickout")) {
            //将sessionId存入队列
            deque.push(sessionId);
            logger.info("将用户的sessionId放入到deque队列中");
            //将用户的sessionId队列缓存
            cache.put(username, deque);
        }

        //如果队列里的sessionId数超出最大会话数，开始踢人
        while (deque.size() > maxSession) {
            this.handleWhile(deque, username);
        }

        //如果被踢出了，直接退出，重定向到踢出后的地址
        if (session.getAttribute("kickout") != null && (Boolean) session.getAttribute("kickout")) {
            //会话被踢出了
            try {
                //退出登录
                subject.logout();
                logger.info("将kickout属性为true的会话真正踢出");
            } catch (Exception e) { //ignore
            }
            try {
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                httpServletResponse.setStatus(HttpServletResponse.SC_OK);
                httpServletResponse.setContentType("text/html;charset=UTF-8");
                //重写向登录页
                WebUtils.issueRedirect(request, response, kickoutUrl);
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
            return false;
        }
        return true;
    }

    private void handleWhile(Deque<Serializable> deque, String username){
        Serializable kickoutSessionId = null;
        if (kickoutAfter) { //如果踢出后者
            kickoutSessionId = deque.removeFirst();
            logger.info("将后登录的用户sessionId从deque队列中删除");
            //踢出后再更新下缓存队列
            cache.put(username, deque);
        } else { //否则踢出前者
            kickoutSessionId = deque.removeLast();
            logger.info("将先登录的用户sessionId从deque队列中删除");
            //踢出后再更新下缓存队列
            cache.put(username, deque);
        }
        try {
            //获取被踢出的sessionId的session对象
            SessionManager sessionManager = SpringUtils.getBean("sessionManager");
            Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
            logger.info("要被踢除的kickoutSession:" + kickoutSession);
            if (kickoutSession != null) {
                //设置会话的kickout属性表示踢出了
                kickoutSession.setAttribute("kickout", true);
                logger.info("将要被踢出的会话session的kickout属性设置为true");
            }
        } catch (Exception e) {//ignore exception
            logger.info("清除对应session报错,无需理会");
        }
    }


}
