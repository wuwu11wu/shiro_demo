package com.zhi.service;

import com.zhi.entity.AuthUser;
import java.util.List;

/**
 * (AuthUser)表服务接口
 *
 * @author makejava
 * @since 2020-05-14 18:10:46
 */
public interface AuthUserService {

    /**
     * 通过ID查询单条数据
     *
     * @param uid 主键
     * @return 实例对象
     */
    AuthUser queryById(Long uid);
    AuthUser findByName(String username);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<AuthUser> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param authUser 实例对象
     * @return 实例对象
     */
    AuthUser insert(AuthUser authUser);

    /**
     * 修改数据
     *
     * @param authUser 实例对象
     * @return 实例对象
     */
    AuthUser update(AuthUser authUser);

    /**
     * 通过主键删除数据
     *
     * @param uid 主键
     * @return 是否成功
     */
    boolean deleteById(Long uid);

}