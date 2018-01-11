package com.money.game.business.service;

import com.money.game.business.dto.manager.PaymentQueryDto;
import com.money.game.business.entity.PaymentEntity;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

/**
 * @author conan
 *         2018/1/5 11:24
 **/
public interface PaymentService {

    /**
     * 初始化交易数据
     */
    PaymentEntity initPayment(String accountOid, String userOid, String tradeType, String orderNo, String status, String direction, BigDecimal amount);

    /**
     * 保存
     */
    PaymentEntity save(PaymentEntity paymentEntity);

    PaymentEntity findByPayNo(String payNo);

    Page<PaymentEntity> findAll(PaymentQueryDto dto);
}
