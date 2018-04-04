package com.nieyue.controller;

import com.nieyue.bean.Permission;
import com.nieyue.bean.RolePermission;
import com.nieyue.exception.AccountLoginException;
import com.nieyue.service.PermissionService;
import com.nieyue.service.RolePermissionService;
import com.nieyue.shiro.ShiroService;
import com.nieyue.shiro.ShiroUtil;
import com.nieyue.util.MyDESutil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 聂跃 on 2018/3/28.
 */
@RestController
@Api(tags={"test"},value="测试管理",description="测试管理")
@RequestMapping("/test")
public class TestController {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    ShiroService shiroService;
    @Autowired
    PermissionService permissionService;
    @Autowired
    RolePermissionService rolePermissionService;
    @Autowired
    ShiroUtil shiroUtil;
    @RequestMapping("/one")
    @ApiOperation(value = "测一测", notes = "测一测")
    boolean getOne(
            @RequestParam("region") Integer region,
            @RequestParam("roleId") Integer roleId,
            @RequestParam("permissionId") Integer permissionId
    ){
        RolePermission rolePermission=new RolePermission();
        rolePermission.setRegion(region);
        rolePermission.setRoleId(roleId);
        rolePermission.setPermissionId(permissionId);
        boolean b=rolePermissionService.addRolePermission(rolePermission);
        return b;
    }



    /**
     * 未登录，shiro应重定向到登录界面，此处返回未登录状态信息由前端控制跳转页面
     * @return
     */
    @RequestMapping(value = "/unauth")
    @ApiOperation(value = "权限注册", notes = "权限注册")
    @ResponseBody
    public boolean unauth() throws Exception {
        boolean b=false;
        b=shiroUtil.initPermission();
        return b;
    }
    /**
     * sessionid
     * @return
     */
    @RequestMapping(value = "/sessionid")
    @ApiOperation(value = "获取sessionId", notes = "获取sessionId")
    @ResponseBody
    public String getSessionId(HttpSession session) {

        return session.getId()+"sdfr";
    }
    static AtomicInteger a=new AtomicInteger(1);
    /**
     * 更新权限
     * @return
     */
    @RequestMapping(value = "/us")
    @ApiOperation(value = "更新权限", notes = "更新权限")
    @ResponseBody
    public Map<String, String> updateShiro() {
        List<Permission> permissionList = permissionService.browsePagingPermission(null, null, null, null, 1, Integer.MAX_VALUE, "permission_id", "asc");
        Map<String, String> m = shiroService.updatePermission(permissionList);
        return m;
    }
    /**
     * login
     * @return
     */
    @RequestMapping(value = "/login")
    @ApiOperation(value = "测试登录", notes = "测试登录")
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
