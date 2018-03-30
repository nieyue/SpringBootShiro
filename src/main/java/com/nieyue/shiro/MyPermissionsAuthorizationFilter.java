package com.nieyue.shiro;

import com.nieyue.exception.CommonRollbackException;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author: 聂跃
 * @description: 对权限过滤
 * @date: 2017/10/24 10:11
 */
public class MyPermissionsAuthorizationFilter extends PermissionsAuthorizationFilter {

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        boolean b = super.isAccessAllowed(request, response, mappedValue);
        System.out.println(999999);
        System.out.println(b);
        return b;
    }

}
