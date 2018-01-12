package com.money.game.business.service.impl;

import com.money.game.business.dao.SmsSendDao;
import com.money.game.business.entity.SmsSendEntity;
import com.money.game.business.service.SmsSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author conan
 *         2018/1/12 11:05
 **/
@Service
public class SmsSendServiceImpl implements SmsSendService {

    @Autowired
    private SmsSendDao smsSendDao;

    public SmsSendEntity initSmsSendService(String type, String content, String errCode, String errMsg, String status) {
        SmsSendEntity smsSendEntity = new SmsSendEntity();
        smsSendEntity.setSmsSendTypes(type);
        smsSendEntity.setNotifyContent(content);
        smsSendEntity.setErrorCode(errCode);
        smsSendEntity.setErrorMessage(errMsg);
        smsSendEntity.setNotifyStatus(status);
        return smsSendEntity;
    }

    public void save(SmsSendEntity smsSendEntity) {
        smsSendDao.save(smsSendEntity);
    }
}
