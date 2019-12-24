package com.evan.wj.interceptor;

import com.evan.wj.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())){

            response.setStatus(HttpStatus.NO_CONTENT.value());

            return true;
        }

        Subject subject = SecurityUtils.getSubject();

        log.info("isAuthenticated: {}, isRemembered:{} ", subject.isAuthenticated(), subject.isRemembered());

        if (!subject.isAuthenticated() && !subject.isRemembered()){

            return false;
        }

        HttpSession httpSession = request.getSession();
        String contextPath = httpSession.getServletContext().getContextPath();
        String[] requireAuthPages = new String[]{
                "index",
        };

        String uri = request.getRequestURI();

        uri = StringUtils.remove(uri, contextPath + "/");

        String page = uri;

        if (begingWith(page, requireAuthPages)) {

            User user = (User) httpSession.getAttribute("user");

            if (user == null) {

                response.sendRedirect("login");

                return false;
            }

        }

        return true;


    }


    private boolean begingWith(String page, String[] requiredAuthPages) {
        boolean result = false;
        for (String requiredAuthPage : requiredAuthPages) {
            if (StringUtils.startsWith(page, requiredAuthPage)) {
                result = true;
                break;
            }
        }
        return result;
    }

}
