package com.money.game.business.dao;

import com.money.game.business.entity.AccountEntity;
import com.money.game.business.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * @author conan
 *         2017/10/26 13:41
 **/
public interface AccountDao extends JpaRepository<AccountEntity, String>, JpaSpecificationExecutor<AccountEntity> {

    AccountEntity findByUserOid(String userOid);


    @Query(value = "select * from T_ACCOUNT where   accountType = ?  order by createTime asc limit 1 ", nativeQuery = true)
    AccountEntity findByAccountType(String accountType);
}


