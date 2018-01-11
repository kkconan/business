package com.money.game.business.service.impl;

import com.money.game.business.constant.DictEnum;
import com.money.game.business.constant.ErrorEnum;
import com.money.game.business.dao.UserDao;
import com.money.game.business.dto.client.RegisterDto;
import com.money.game.business.entity.UserEntity;
import com.money.game.business.exception.BizException;
import com.money.game.business.service.UserService;
import com.money.game.core.util.Digests;
import com.money.game.core.util.PwdUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author conan
 *         2018/1/5 9:57
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public UserEntity addUser(RegisterDto dto) {
        UserEntity entity = new UserEntity();
        entity.setUserAccount(dto.getUserAccount());
        if (!StringUtils.isEmpty(dto.getUserPwd())) {
            entity.setSalt(Digests.genSalt());
            entity.setUserPwd(PwdUtil.encryptPassword(dto.getUserPwd(), entity.getSalt()));
        }
        entity.setPhone(dto.getPhone());
        entity.setUserType(DictEnum.USER_TYPE_01.getCode());
        entity.setStatus(DictEnum.USER_STATUS_NORMAL.getCode());
        userDao.save(entity);
        return entity;
    }

    @Override
    public UserEntity findByUserAccountOrPhone(String userAccount, String phone) {
        UserEntity userEntity = userDao.findByUserAccountOrPhone(userAccount, phone);
        if (userEntity == null) {
            throw new BizException(ErrorEnum.USER_NOT_FOUND);
        }
        return userEntity;
    }

    @Override
    public UserEntity findByOid(String oid) {
        UserEntity userEntity = userDao.findOne(oid);
        if (userEntity == null) {
            throw new BizException(ErrorEnum.USER_NOT_FOUND);
        }
        if(DictEnum.USER_STATUS_FORBIDDER.getCode()
        .equals(userEntity.getStatus())){
            throw new BizException(ErrorEnum.USER_NOT_FORBIDDEN);
        }
        return userEntity;
    }
}
