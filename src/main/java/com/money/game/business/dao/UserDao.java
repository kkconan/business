package com.money.game.business.dao;

import com.money.game.business.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author conan
 *         2017/10/26 13:41
 **/
public interface UserDao extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity> {

    UserEntity findByUserAccountOrPhone(String userAccount,String phone);

}


