package com.money.game.business.service;

import com.money.game.business.dto.client.RegisterDto;
import com.money.game.business.entity.UserEntity;

/**
 * @author conan
 *         2018/1/5 9:57
 **/
public interface UserService {

    /**
     * 创建用户账号
     */
    UserEntity addUser(RegisterDto dto);

    /**
     * 根据账号或者手机号查询
     */
    UserEntity findByUserAccountOrPhone(String userAccount,String phone);

    UserEntity findByOid(String oid);
}
