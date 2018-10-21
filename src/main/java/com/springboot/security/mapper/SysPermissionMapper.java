package com.springboot.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springboot.security.entity.SysPermission;
import com.springboot.security.entity.ins.PermissionTreeIns;
import com.springboot.security.entity.ins.SysPermissionIns;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zt
 * @since 2018-09-29
 */
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    /**
     * 通过用户id 查询其权限集合
     * @param userId
     * @return
     */
    List<SysPermission>getPermissionByUserId(@Param("userId")Integer userId);

    /**
     * 权限树
     * @param parentId 指定根菜单
     * @return
     */
    List<PermissionTreeIns> getPermissionTree(Page page, @Param("parentId") Integer parentId);

    /**
     * 得到某个角色的权限集合
     * @param roleId
     * @return
     */
    public List<SysPermission>getPermissionByRoleId(@Param("roleId")Integer roleId);

    List<SysPermissionIns> getPermissionAndRoleByCode(@Param("code")String code);
}
