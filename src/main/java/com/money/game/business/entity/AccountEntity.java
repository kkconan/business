package com.money.game.business.entity;

import com.money.game.basic.component.ext.hibernate.UUID;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 用户资金账户
 * @author conan
 *         2018/1/4 16:28
 **/
@Entity
@Table(name = "T_ACCOUNT")
@lombok.Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class AccountEntity extends UUID implements Serializable {
    private static final long serialVersionUID = 6743566467342671039L;

    /**
     * 账户号
     */
    private String accountNo;
    /**
     * 用户ID
     */
    private String userOid;
    /**
     * 01--基本账户，02--提现冻结账户，03--充值冻结账户，04:投注冻结账户，05：平台账户
     */
    private String accountType;
    /**
     * 账户余额
     */
    private BigDecimal balance  = BigDecimal.ZERO;

    /**
     * 提现冻结
     */
    private BigDecimal withdrawFrozenBalance = BigDecimal.ZERO;

    /**
     * 充值冻结(充值到账，但不能提现)
     */
    private BigDecimal rechargeFrozenBalance = BigDecimal.ZERO;

    /**
     * 01：正常，02：冻结
     */
    private String status;

    private Timestamp updateTime;

    private Timestamp createTime;
}
