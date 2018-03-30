package com.nieyue.shiro;

import com.nieyue.exception.CommonNotRollbackException;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Created by 聂跃 on 2018/3/29.
 * 对没登录的过滤
 */
public class MyFormAuthenticationFilter extends FormAuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        boolean b=super.onAccessDenied(request, response);
        System.out.println(2333333);
        System.out.println(b);
        return b;
    }
}
