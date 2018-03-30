package com.nieyue.controller;

import com.nieyue.bean.Permission;
import com.nieyue.exception.AccountLoginException;
import com.nieyue.exception.AccountNotAuthException;
import com.nieyue.exception.CommonNotRollbackException;
import com.nieyue.exception.CommonRollbackException;
import com.nieyue.shiro.ShiroService;
import com.nieyue.util.MyDESutil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 聂跃 on 2018/3/28.
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    ShiroService shiroService;
    @RequestMapping("/one")
    String getOne(){
        return "sd22f232";
    }



    /**
     * 未登录，shiro应重定向到登录界面，此处返回未登录状态信息由前端控制跳转页面
     * @return
     */
    @RequestMapping(value = "/unauth")
    @ResponseBody
    public void unauth() {
        throw new AccountNotAuthException();//没有权限
    }
    /**
     * sessionid
     * @return
     */
    @RequestMapping(value = "/sessionid")
    @ResponseBody
    public String getSessionId(HttpSession session) {

        return session.getId();
    }
    static AtomicInteger a=new AtomicInteger(1);
    /**
     * sessionid
     * @return
     */
    @RequestMapping(value = "/us")
    @ResponseBody
    public Map<String, String> updateShiro() {
        Set<Permission> s=new HashSet<>();
        Permission p = new Permission();
        a.incrementAndGet();
        p.setRoute("/a"+a.toString());
        s.add(p);
        Map<String, String> m = shiroService.updatePermission(s);
        return m;
    }
    /**
     * login
     * @return
     */
    @RequestMapping(value = "/login")
    @ResponseBody
    public String login(HttpSession session) {
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("15111336587", MyDESutil.getMD5("123456"));
        try {
            currentUser.login(token);

        } catch (AuthenticationException e) {
            throw new AccountLoginException();
        }
                //初始化基准收益单价
        BoundValueOperations<String, String> etfubp=stringRedisTemplate.boundValueOps("ttt");
        if(etfubp.size()<=0){
            etfubp.set("0");
            System.out.println(etfubp.get());
        }
        System.out.println(SecurityUtils.getSubject().getSession().getId());
        System.out.println(SecurityUtils.getSubject().getSession().getAttribute("account"));
        System.out.println(session.getAttribute("account"));
        return session.getId();
    }
}
