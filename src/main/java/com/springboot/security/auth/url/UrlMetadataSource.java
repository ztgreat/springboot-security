package com.springboot.security.auth.url;

import com.springboot.security.auth.TokenManager;
import com.springboot.security.auth.UserToken;
import com.springboot.security.auth.url.MyWildcardPermission;
import com.springboot.security.auth.url.Permission;
import com.springboot.security.entity.SysPermission;
import com.springboot.security.entity.SysRole;
import com.springboot.security.entity.ins.SysPermissionIns;
import com.springboot.security.service.SysPermissionService;
import com.springboot.security.util.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.*;


/**
 * url资源权限处理
 *
 * 获取当前url需要的权限
 *
 * 这部分可以利用缓存
 */

@Component

public class UrlMetadataSource implements FilterInvocationSecurityMetadataSource {


    /**
     * 请求前缀,类似：/api/test/hello
     */
    private static  String urlPrefix;

    /**
     * 退出登录url
     */
    private static  String urlLogout;

    protected static final String PART_DIVIDER_TOKEN = ":";

    @Autowired
    private SysPermissionService permissionService;

    public static String getUrlPrefix() {
        return urlPrefix;
    }

    public static void setUrlPrefix(String urlPrefix) {
        UrlMetadataSource.urlPrefix = urlPrefix;
    }

    public static String getUrlLogout() {
        return urlLogout;
    }

    public static void setUrlLogout(String urlLogout) {
        UrlMetadataSource.urlLogout = urlLogout;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) {


        //判断用户是否登录，如果没有登录，则需要登录权限
        UserToken token=null;
        try {
            token=TokenManager.getToken();
        }catch (Exception ignore){

        }
        if(token ==null){
            //未登陆
            return SecurityConfig.createList("ROLE_LOGIN");
        }

        //请求url
        String requestUrl = ((FilterInvocation) o).getRequestUrl();

        if(this.urlLogout !=null && requestUrl.startsWith(this.urlLogout)){
            return SecurityConfig.createList("ROLE_LOGIN");
        }

        //处理url
        //例如： /api/menu/getMenu ->  menu:getMenu
        String url =resoverUrl(((FilterInvocation) o).getRequest().getContextPath(),requestUrl);

        String []urlArray=url.split(PART_DIVIDER_TOKEN);

        if(urlArray!=null && urlArray.length>0){

            //获取该访问相关的资源
            List<SysPermissionIns> allPermissions = permissionService.getPermissionAndRoleByCode(urlArray[0]);

            //权限字符串
            List<String>permissionStr = new ArrayList<>();

            for(SysPermissionIns permission:allPermissions){
                permissionStr.add(permission.getCode());
            }

            //构造访问url的权限字符串
            MyWildcardPermission requestUrlPermission = new MyWildcardPermission(url);


            //构造需要对比的权限字符集合
            Collection<MyWildcardPermission> resolvedPermission = getPermissions(allPermissions);

            if (resolvedPermission != null && !resolvedPermission.isEmpty()) {

                ArrayList<String>rolesArray = new ArrayList<>();

                for (MyWildcardPermission perm : resolvedPermission) {
                    if (perm.implies(requestUrlPermission)) {

                        //匹配成功
                        //查询角色
                        List<SysRole> roles=perm.getRoles();
                        for (int i = 0; i < perm.getRoles().size(); i++) {
                            rolesArray.add(roles.get(i).getCode());
                        }
                    }
                }

                if(rolesArray.size()>0){
                    String []temp = new String[rolesArray.size()];
                    for(int i=0;i<rolesArray.size();i++){
                        temp[i]=rolesArray.get(i);
                    }
                    return SecurityConfig.createList(temp);
                }
            }

        }

        //没有匹配上的资源，拒绝访问
        return SecurityConfig.createList("ROLE_Denied");
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


        if(url!=null){
            int param = url.indexOf("?");
            if(param>-1){
                url = url.substring(0,param);
            }
        }

        if (null != url && url.startsWith(basePath)) {
            url = url.replaceFirst(basePath, "");
        }


        if(this.urlPrefix !=null && url.startsWith(this.urlPrefix)){
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
     * @param sysPermissions
     * @return
     */
    protected Collection<MyWildcardPermission> getPermissions(Collection<SysPermissionIns> sysPermissions) {
        Set<MyWildcardPermission> permissions = new HashSet<MyWildcardPermission>();


        Collection<MyWildcardPermission>perms = resolvePermissions(sysPermissions);
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
     * @param sysPermissions
     * @return
     */
    private Collection<MyWildcardPermission> resolvePermissions(Collection<SysPermissionIns> sysPermissions) {
        Collection<MyWildcardPermission> perms = Collections.emptySet();
        if (!CollectionUtil.isEmpty(sysPermissions)) {
            perms = new LinkedHashSet<MyWildcardPermission>(sysPermissions.size());
            for (SysPermissionIns sysPermissionIns : sysPermissions) {
                MyWildcardPermission permission = new MyWildcardPermission(sysPermissionIns.getCode());

                permission.setRoles(sysPermissionIns.getRoles());
                perms.add(permission);
            }
        }
        return perms;
    }

}
