package com.zhi.service.impl;

import com.zhi.Util.KeyNameUtil;
import com.zhi.entity.AuthUser;
import com.zhi.dao.AuthUserDao;
import com.zhi.service.AuthUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (AuthUser)表服务实现类
 *
 * @author makejava
 * @since 2020-05-14 18:10:46
 */
@Service("authUserService")

public class AuthUserServiceImpl implements AuthUserService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    //转换类型 redisTemplate.opsForValue()======》string
//    @Resource(name = "redisTemplate")
    @Resource
    private ValueOperations<String, AuthUser> string;

    @Resource
    private AuthUserDao authUserDao;

    /**
     * 通过ID查询单条数据
     *
     * @param uid 主键
     * @return 实例对象
     */

    //hash
    @Override
    public AuthUser queryById(Long uid) {

        //缓存中查找
        AuthUser authUser = (AuthUser) redisTemplate.opsForHash().get(KeyNameUtil.AUTHUSER, uid);
        //判断是否存在
        if (null == authUser) {
            //查询数据库
            authUser = this.authUserDao.queryById(uid);
            /*
            h 用户实体 user
            hk 用户主键 id
            hv 整个对象 authuser
             */
            redisTemplate.opsForHash().put(KeyNameUtil.AUTHUSER, uid, authUser);

        }
        return authUser;
    }


    //redis利用string进行缓存
    @Override
    public AuthUser findByName(String username) {

//        //字符串序列化
//        RedisSerializer redisSerializer  = new StringRedisSerializer();
//        redisTemplate.setKeySerializer(redisSerializer);

        //从缓存中查找
//        AuthUser authUser = (AuthUser) redisTemplate.opsForValue().get(username);
        AuthUser authUser = string.get(username);
        if (null == authUser) {
            //查询数据库
            authUser = this.authUserDao.findByName(username);
            //放入redis中
            redisTemplate.opsForValue().set(username, authUser);
        }
        return authUser;
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<AuthUser> queryAllByLimit(int offset, int limit) {
        return this.authUserDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param authUser 实例对象
     * @return 实例对象
     */
    @Override
    public AuthUser insert(AuthUser authUser) {
        this.authUserDao.insert(authUser);
        return authUser;
    }

    /**
     * 修改数据
     *
     * @param authUser 实例对象
     * @return 实例对象
     */
    @Override
    public AuthUser update(AuthUser authUser) {
        this.authUserDao.update(authUser);
        return this.queryById(authUser.getUid());
    }

    /**
     * 通过主键删除数据
     *
     * @param uid 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long uid) {
        return this.authUserDao.deleteById(uid) > 0;
    }
}