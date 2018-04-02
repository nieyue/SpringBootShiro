package com.nieyue.controller;

import com.nieyue.bean.Permission;
import com.nieyue.bean.RolePermission;
import com.nieyue.exception.AccountLoginException;
import com.nieyue.exception.AccountNotAuthException;
import com.nieyue.exception.CommonNotRollbackException;
import com.nieyue.exception.CommonRollbackException;
import com.nieyue.service.PermissionService;
import com.nieyue.service.RolePermissionService;
import com.nieyue.shiro.ShiroService;
import com.nieyue.util.HttpClientUtil;
import com.nieyue.util.MyDESutil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.models.Tag;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigInteger;
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
    @Value("${myPugin.activationCodeMallProjectDomainUrl}")
    String activationCodeMallProjectDomainUrl;
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
        //String s = HttpClientUtil.doGet("http://server.laoyeshuo.cn/v2/api-docs");
        String s = HttpClientUtil.doGet(activationCodeMallProjectDomainUrl+"/v2/api-docs");
        JSONObject json = JSONObject.fromObject(s);
        Map<String, Class> classMap = new HashMap<String, Class>();
        classMap.put("tags", Tag.class);
        Map<String,Class> pathMap = new HashMap<String, Class>();
        classMap.put("paths",pathMap.getClass());
        Swagger swagger= (Swagger) JSONObject.toBean(json, Swagger.class,classMap);
        List<Tag> tagslist=swagger.getTags();
        Map<String,Path> pathsMap=swagger.getPaths();
        for (Map.Entry<String, Path>  entry: pathsMap.entrySet()) {
            //System.out.println(entry.getKey());
            Permission permission =new Permission();
            // System.out.println(entry.getValue());
            JSONObject js=JSONObject.fromObject(entry.getValue());
            //System.out.println(js.get("post"));
            JSONObject ss=JSONObject.fromObject(js.get("post"));
            //System.out.println(ss.get("summary"));
            JSONArray sss=JSONArray.fromObject(ss.get("tags"));
            for (int i = 0; i < tagslist.size(); i++) {
                if(sss.get(0).equals(tagslist.get(i).getName())){
                   // System.out.println(sss.get(0));
                    //System.out.println(tagslist.get(i).getDescription());
                    permission.setManagerName(tagslist.get(i).getDescription());//权限管理名称
                    permission.setName(ss.get("summary").toString());//权限名称
                    permission.setRoute(entry.getKey());//权限路由
                    permission.setType(1);//默认鉴权
                     b=permissionService.addPermission(permission);
                   // System.out.println("---------------------");
                }
            }
        }
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

        return session.getId();
    }
    static AtomicInteger a=new AtomicInteger(1);
    /**
     * sessionid
     * @return
     */
    @RequestMapping(value = "/us")
    @ApiOperation(value = "更新权限", notes = "更新权限")
    @ResponseBody
    public Map<String, String> updateShiro() {
        List<Permission> s=new ArrayList<>();
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
