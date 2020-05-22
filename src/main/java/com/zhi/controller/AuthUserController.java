package com.zhi.controller;

import com.zhi.entity.AuthUser;
import com.zhi.service.AuthUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (AuthUser)表控制层
 *
 * @author makejava
 * @since 2020-05-14 18:10:46
 */
@RestController
@RequestMapping("authUser")
public class AuthUserController {


    /**
     * 服务对象
     */
    @Resource
    private AuthUserService authUserService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public AuthUser selectOne(Long id) {
        return this.authUserService.queryById(id);
    }

}