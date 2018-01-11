package com.money.game.business.service.impl;

import com.money.game.basic.common.SeqGenerator;
import com.money.game.business.constant.DictEnum;
import com.money.game.business.constant.ErrorEnum;
import com.money.game.business.dao.AccountDao;
import com.money.game.business.entity.AccountEntity;
import com.money.game.business.exception.BizException;
import com.money.game.business.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author conan
 *         2018/1/5 11:24
 **/
@Slf4j
@Service
public class AccountServiceImpl implements AccountService{

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private SeqGenerator seqGenerator;

    @Override
    public AccountEntity initAccount(String userOid, String userType){
        AccountEntity accountEntity = new AccountEntity();
        String accountNo = this.seqGenerator.next(DictEnum.ACCOUNT_NO_PREFIX.getCode());
        accountEntity.setAccountNo(accountNo);
        accountEntity.setUserOid(userOid);
        accountEntity.setAccountType(userType);
        accountEntity.setStatus(DictEnum.USER_STATUS_NORMAL.getCode());
        return accountEntity;
    }

    @Override
    public AccountEntity save(AccountEntity accountEntity){
        return accountDao.save(accountEntity);
    }

    @Override
    public AccountEntity findByUserOid(String userOid) {
        AccountEntity accountEntity = accountDao.findByUserOid(userOid);
        if (accountEntity == null) {
            throw new BizException(ErrorEnum.USER_NOT_FOUND);
        }
        if(DictEnum.USER_STATUS_FORBIDDER.getCode()
                .equals(accountEntity.getStatus())){
            throw new BizException(ErrorEnum.USER_NOT_FORBIDDEN);
        }
        return accountEntity;
    }

    @Override
    public AccountEntity findById(String oid){
        return accountDao.findOne(oid);
    }

    @Override
    public AccountEntity findByAccountType(String accountType) {
        return accountDao.findByAccountType(accountType);
    }
}
