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
 * 资金变动明细
 *
 * @author conan
 *         2018/1/4 16:39
 **/
@Entity
@Table(name = "T_ORDER")
@lombok.Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class OrderEntity extends UUID implements Serializable {

    private static final long serialVersionUID = 3736223814138137799L;

    /**
     * 账户号
     */
    private String accountOid;

    /**
     * 用户ID
     */
    private String userOid;


    /**
     * 订单号（投注才会产生）
     */
    private String orderNo;


    /**
     * 状态 01:已投注,02:已完成
     */
    private String status;

    /**
     * 订单金额
     */
    private BigDecimal amount;

    /**
     * 赛事id
     */
    private String gameId;

    /**
     * 投注项
     */
    private String proKey;

    /**
     * 更新时间
     */
    private Timestamp updateTime;

    /**
     * 创建时间
     */
    private Timestamp createTime;

}
