package com.money.game.business.service;

import com.money.game.business.entity.AccountEntity;

/**
 * @author conan
 *         2018/1/5 11:23
 **/
public interface AccountService {

    /**
     * 初始化基本账户
     */
    AccountEntity initAccount(String userOid, String userType);

    /**
     * 保存
     */
    AccountEntity save(AccountEntity accountEntity);

    AccountEntity findByUserOid(String userOid);

    AccountEntity findById(String oid);

    AccountEntity findByAccountType(String accountType);
}
