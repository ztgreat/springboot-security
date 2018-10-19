package com.springboot.security.auth.url;

import com.springboot.security.auth.TokenManager;
import com.springboot.security.auth.UserToken;
import com.springboot.security.auth.url.MyWildcardPermission;
import com.springboot.security.auth.url.Permission;
import com.springboot.security.entity.SysPermission;
import com.springboot.security.entity.SysRole;
import com.springboot.security.service.SysPermissionService;
import com.springboot.security.util.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UrlMetadataSource implements FilterInvocationSecurityMetadataSource {


    private static  String urlPrefix="/api";

    protected static final String PART_DIVIDER_TOKEN = ":";

    @Autowired
    private SysPermissionService permissionService;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) {

        UserToken token=null;
        try {
            token=TokenManager.getToken();
        }catch (Exception ignore){

        }
        if(token ==null){
            //未登陆
            return SecurityConfig.createList("ROLE_LOGIN");
        }


        String requestUrl = ((FilterInvocation) o).getRequestUrl();
        String url =resoverUrl(((FilterInvocation) o).getRequest().getContextPath(),requestUrl);

        String []urlArray=url.split(PART_DIVIDER_TOKEN);

        if(urlArray!=null && urlArray.length>0){

            //获取该访问相关的资源
            List<SysPermission> allPermissions = permissionService.getPermissionByCode(urlArray[0]);

            //权限字符串
            List<String>permissionStr = new ArrayList<>();

            for(SysPermission permission:allPermissions){
                permissionStr.add(permission.getCode());
            }

            //构造访问url的权限字符串
            MyWildcardPermission requestUrlPermission = new MyWildcardPermission(url);


            //构造需要对比的权限字符集合
            Collection<Permission> resolvedPermission = getPermissions(null,permissionStr);

            if (resolvedPermission != null && !resolvedPermission.isEmpty()) {
                for (Permission perm : resolvedPermission) {
                    if (perm.implies(requestUrlPermission)) {

                        //匹配成功

                        //根据permission 查询角色
                        List<SysRole> roles=new ArrayList<>();
                        int size = roles.size();
                        String[] values = new String[size];
                        for (int i = 0; i < size; i++) {
                            values[i] = roles.get(i).getCode();
                        }
                        return SecurityConfig.createList("ROLE_LOGIN");
                    }
                }
            }

        }




        //没有匹配上的资源，都是登录访问
        return SecurityConfig.createList("ROLE_LOGIN");
    }
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }
    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }


    /**
     * 解析请求url
     * @param basePath
     * @param url
     * @return
     */
    public String resoverUrl(String basePath,String url){
        if (null != url && url.startsWith(basePath)) {
            url = url.replaceFirst(basePath, "");
        }


        if(url.startsWith(this.urlPrefix)){
            url = url.replaceFirst(this.urlPrefix, "");
        }


        // 不存在"/"地址不符合地址要求，返回没有权限
        if (url.indexOf("/") < 0) {
            return null;
        }
        if (url.substring(0, 1).equals("/")) {
            url = url.replaceFirst("/", "").replaceAll("/", ":");
        } else {
            url = url.replaceAll("/", ":");
        }
        if (StringUtils.isBlank(url)) {
            return null;
        }
        return url;
    }


    /**
     * 构造url 权限字符串
     * @param perms
     * @return
     */
    protected Collection<Permission> getPermissions(Collection<Permission> perms,Collection<String> stringPerms) {
        Set<Permission> permissions = new HashSet<Permission>();

        if (!CollectionUtil.isEmpty(perms)) {
            permissions.addAll(perms);
        }
        perms = resolvePermissions(stringPerms);
        if (!CollectionUtil.isEmpty(perms)) {
            permissions.addAll(perms);
        }

        if (permissions.isEmpty()) {
            return Collections.emptySet();
        } else {
            return Collections.unmodifiableSet(permissions);
        }
    }

    /**
     * 构造url 权限字符串
     * @param stringPerms
     * @return
     */
    private Collection<Permission> resolvePermissions(Collection<String> stringPerms) {
        Collection<Permission> perms = Collections.emptySet();
        if (!CollectionUtil.isEmpty(stringPerms)) {
            perms = new LinkedHashSet<Permission>(stringPerms.size());
            for (String strPermission : stringPerms) {
                Permission permission = new MyWildcardPermission(strPermission);
                perms.add(permission);
            }
        }
        return perms;
    }

}
