package com.nieyue.shiro;

import com.nieyue.bean.Account;
import com.nieyue.exception.AccountLoginException;
import com.nieyue.service.AccountService;
import com.nieyue.util.MyDESutil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.apache.shiro.web.filter.mgt.DefaultFilter.user;

/**
 * @author: 聂跃
 * @description: 自定义Realm
 * @date: 2017/10/24 10:06
 */
public class AccountRealm extends AuthorizingRealm {
    private Logger logger = LoggerFactory.getLogger(AccountRealm.class);

    @Autowired
    private AccountService accountService;

    /**
     * 登陆之后调用,注册当前用户角色、权限
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Session session = SecurityUtils.getSubject().getSession();
        //查询用户的权限
        Account account = (Account) session.getAttribute("account");
        //Object account = session.getAttribute("account");

        logger.info(account.toString());
        //为当前用户设置角色和权限
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        Set<String> set=new HashSet<>();
        set.add("a");
        set.add("/account/list");
        set.add("c");
        logger.info("sddddddddd");
//        Set<String> set2=new HashSet<>();
//        set2.add("超级管理员");
//        set2.add("第三方");
//        authorizationInfo.addRoles(set2);
        authorizationInfo.addStringPermissions(set);
        return authorizationInfo;
    }

    /**
     * 验证当前登录的Subject
     * LoginController.login()方法中执行Subject.login()时 执行此方法
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        // 获取用户密码
        String loginName = (String) authcToken.getPrincipal();
        String password = new String((char[]) authcToken.getCredentials());
        Account account = accountService.loginAccount(loginName, password, null);
        if (account == null) {
            throw new AccountLoginException();//账户或密码错误
        }

        String adminName=account.getPhone();
        if(ObjectUtils.isEmpty(adminName)){
            adminName=account.getEmail();
        }
        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                adminName,
                account.getPassword(),
                //ByteSource.Util.bytes("salt"), //salt=username+salt,采用明文访问时，不需要此句
                getName()
        );
        //将用户信息放入session中
        SecurityUtils.getSubject().getSession().setAttribute("account", account);

        //处理session
//        SessionsSecurityManager securityManager = (SessionsSecurityManager) SecurityUtils.getSecurityManager();
//        DefaultSessionManager sessionManager = (DefaultSessionManager) securityManager.getSessionManager();
//        Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();//获取当前已登录的用户session列表
//        for (Session session : sessions) {
//            //清除该用户以前登录时保存的session
//            //如果和当前session是同一个session，则不剔除
//            if (SecurityUtils.getSubject().getSession().getId().equals(session.getId())) {
//                continue;
//            }
//            Account a = (Account)(session.getAttribute("account"));
//            if (a != null) {
//                Integer aId = a.getAccountId();
//                if (account.getAccountId().equals(aId)) {
//                    sessionManager.getSessionDAO().delete(session);
//                }
//            }
//        }
        return authenticationInfo;
    }
}
