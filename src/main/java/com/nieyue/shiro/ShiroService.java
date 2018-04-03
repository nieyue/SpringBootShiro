package com.nieyue.shiro;

import com.nieyue.bean.Permission;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 服务
 * @author 作者: 涅羽
 * @date 创建时间：2018
 */
@Service
@Configuration
public class ShiroService {

    @Autowired
    ShiroConfiguration shiroConfiguration;
    @Autowired
    ShiroFilterFactoryBean shiroFilterFactoryBean;
    //@Autowired
   // PermissionService permissionService;
    /**
     * 重新加载权限
     */
    public Map<String, String> updatePermission(List<Permission> s) {
        synchronized (shiroFilterFactoryBean) {

            AbstractShiroFilter shiroFilter = null;
            try {
                shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean
                        .getObject();
            } catch (Exception e) {
                throw new RuntimeException(
                        "权限修改错误!");
            }

            PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter
                    .getFilterChainResolver();
            DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver
                    .getFilterChainManager();

            // 清空老的权限控制
            manager.getFilterChains().clear();

            shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
            shiroFilterFactoryBean
                    .setFilterChainDefinitionMap(shiroConfiguration.loadFilterChainDefinitions(s));
            // 重新构建生成
            Map<String, String> chains = shiroFilterFactoryBean
                    .getFilterChainDefinitionMap();
            for (Map.Entry<String, String> entry : chains.entrySet()) {
                String url = entry.getKey();
                String chainDefinition = entry.getValue().trim()
                        .replace(" ", "");
                manager.createChain(url, chainDefinition);
            }
           // System.err.println(chains);
           // System.err.println("更新权限成功！！");
            return chains;
        }
    }
}
