package com.evan.wj.filter;

import com.evan.wj.service.AdminPermissionService;
import com.evan.wj.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;


import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;


@Slf4j
public class URLPathMatchingFilter extends PathMatchingFilter {

    @Autowired
    AdminPermissionService adminPermissionService;

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;


        // 放行 options 请求
        if (HttpMethod.OPTIONS.toString().equals((httpServletRequest).getMethod())) {
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            return true;
        }

        if (null==adminPermissionService) {
            adminPermissionService = SpringContextUtils.getContext().getBean(AdminPermissionService.class);
        }

        String requestAPI = getPathWithinApplication(request);
        log.info("访问接口：" + requestAPI);


        Subject subject = SecurityUtils.getSubject();

        if (!subject.isAuthenticated()){

            log.error("需要登录");

            return false;
        }

        boolean needFilter = adminPermissionService.needFilter(requestAPI);
        if (!needFilter){

            log.info("接口：" + requestAPI + "无需权限");

            return true;
        }else {
            System.out.println("验证访问权限：" + requestAPI);
            // 判断当前用户是否有相应权限
            boolean hasPermission = false;
            String username = subject.getPrincipal().toString();
            Set<String> permissionAPIs = adminPermissionService.listPermissionURLsByUser(username);
            for (String api : permissionAPIs) {
                if (api.equals(requestAPI)) {
                    hasPermission = true;
                    break;
                }
            }

            if (hasPermission) {
                System.out.println("访问权限：" + requestAPI + "验证成功");
                return true;
            } else {
                System.out.println("当前用户没有访问接口" + requestAPI + "的权限");
                return false;
            }

        }


    }
}
