package com.money.game.business.dao;

import com.money.game.business.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author conan
 *         2017/10/26 13:41
 **/
public interface PaymentDao extends JpaRepository<PaymentEntity, String>, JpaSpecificationExecutor<PaymentEntity> {

    PaymentEntity findByPayNo(String payNo);
}


