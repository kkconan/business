package com.money.game.business.vo.client;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author conan
 *         2018/1/12 10:03
 **/
@Data
public class PaymentQueryVo implements Serializable{

    private static final long serialVersionUID = 9026691857452644707L;
    /**
     * 账户号
     */
    private String accountOid;

    /**
     * 用户ID
     */
    private String userOid;

    /**
     * 交易类型,01:注册赠送,02:充值,03:提现,04:投注,05:盈利
     */
    private String tradeType;

    /**
     * 订单号（投注才会产生）
     */
    private String orderNo;

    /**
     * 支付单号
     */
    private String payNo;

    /**
     * 状态 01:已创建,02:待审核,03:已完成,04:退款中,05:已退款
     */
    private String status;

    /**
     * 审核状态 01:未审核,02:审核通过,03:审核拒绝
     */
    private String auditStatus;

    /**
     * 金额方向，01:收入 02:支出
     */
    private String direction;

    /**
     * 订单金额
     */
    private BigDecimal amount;

    /**
     * 赛事id
     */
    private String gameId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 更新时间
     */
    private Timestamp updateTime;

    /**
     * 创建时间
     */
    private Timestamp createTime;
}
