package com.money.game.business.biz;

import com.money.game.business.constant.DictEnum;
import com.money.game.business.constant.ErrorEnum;
import com.money.game.business.dto.manager.PaymentQueryDto;
import com.money.game.business.dto.manager.PaymentVerifyDto;
import com.money.game.business.entity.AccountEntity;
import com.money.game.business.entity.PaymentEntity;
import com.money.game.business.exception.BizException;
import com.money.game.business.service.AccountService;
import com.money.game.business.service.PaymentService;
import com.money.game.core.constant.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author conan
 *         2018/1/11 13:38
 **/
@Component
@Slf4j
public class PaymentBiz {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private AccountService accountService;


    public ResponseData deposit(String userId, BigDecimal amount) {
        log.info("充值,userId={},amount={}", userId, amount);
        AccountEntity accountEntity = accountService.findByUserOid(userId);
        //增加交易记录
        PaymentEntity paymentEntity = paymentService.initPayment(accountEntity.getUserOid(), userId, DictEnum.TRADE_TYPE_02.getCode(), null, DictEnum.PAY_STATUS_01.getCode(), DictEnum.PAY_DIRECTION_01.getCode(), amount);
        paymentEntity.setAuditStatus(DictEnum.PAY_AUDIT_STATUS_01.getCode());
        paymentService.save(paymentEntity);
        //更新充值冻结
        accountEntity.setRechargeFrozenBalance(accountEntity.getRechargeFrozenBalance().add(amount));
        accountService.save(accountEntity);
        return ResponseData.success();
    }
    public ResponseData withDraw(String userId, BigDecimal amount) {
        log.info("提现,userId={},amount={}", userId, amount);
        AccountEntity accountEntity = accountService.findByUserOid(userId);
        if(accountEntity.getBalance().compareTo(amount) < 0){
            throw new BizException(ErrorEnum.ACCOUNT_BALANCE_TOO_LITTLE);
        }
        //增加交易记录
        PaymentEntity paymentEntity = paymentService.initPayment(accountEntity.getUserOid(), userId, DictEnum.TRADE_TYPE_03.getCode(), null, DictEnum.PAY_STATUS_01.getCode(), DictEnum.PAY_DIRECTION_02.getCode(), amount);
        paymentEntity.setAuditStatus(DictEnum.PAY_AUDIT_STATUS_01.getCode());
        paymentService.save(paymentEntity);
        //更新提现冻结
        accountEntity.setBalance(accountEntity.getBalance().subtract(amount));
        accountEntity.setWithdrawFrozenBalance(accountEntity.getWithdrawFrozenBalance().add(amount));
        accountService.save(accountEntity);
        return ResponseData.success();
    }

    public ResponseData list(PaymentQueryDto dto) {

        Page<PaymentEntity> page = paymentService.findAll(dto);

        return ResponseData.success(page.getContent(), dto.getCurrentPage(), dto.getPageSize(), page.getTotalElements());
    }

    public ResponseData verify(PaymentVerifyDto dto) {
        log.info("审核,dto={}", dto);
        PaymentEntity paymentEntity = paymentService.findByPayNo(dto.getPayNo());
        paymentEntity.setAuditStatus(dto.getAuditStatus());
        paymentEntity.setStatus(DictEnum.PAY_STATUS_03.getCode());
        paymentEntity.setRemark(dto.getRemark());
        paymentService.save(paymentEntity);
        AccountEntity accountEntity = accountService.findByUserOid(paymentEntity.getUserOid());
        //充值
        if (DictEnum.TRADE_TYPE_02.getCode().equals(paymentEntity.getTradeType())) {
            //通过
            if (DictEnum.PAY_AUDIT_STATUS_02.getCode().equals(dto.getAuditStatus())) {
                accountEntity.setBalance(accountEntity.getBalance().add(paymentEntity.getAmount()));
            }
            accountEntity.setRechargeFrozenBalance(accountEntity.getRechargeFrozenBalance().subtract(paymentEntity.getAmount()));
            //提现
        } else if (DictEnum.TRADE_TYPE_03.getCode().equals(paymentEntity.getTradeType())) {
            //拒绝
            if (DictEnum.PAY_AUDIT_STATUS_03.getCode().equals(dto.getAuditStatus())) {
                accountEntity.setBalance(accountEntity.getBalance().add(paymentEntity.getAmount()));
            }
            accountEntity.setWithdrawFrozenBalance(accountEntity.getWithdrawFrozenBalance().subtract(paymentEntity.getAmount()));
        }
        accountService.save(accountEntity);
        return ResponseData.success();
    }
}
