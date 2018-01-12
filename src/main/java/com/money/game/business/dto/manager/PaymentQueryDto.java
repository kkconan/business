package com.money.game.business.dto.manager;

import lombok.Data;

import java.io.Serializable;

/**
 * @author conan
 *         2018/1/11 15:15
 **/
@Data
public class PaymentQueryDto implements Serializable{
    private static final long serialVersionUID = 1702795473308350543L;

    /**
     * 查询总数
     */
    private Integer pageSize = 10;

    /**
     * 查询页码
     */
    private Integer currentPage = 1;

    /**
     * 用户id
     */
    private String userOid;

    /**
     * 账户号
     */
    private String userAccount;

    /**
     * 用户ID
     */
    private String phone;

    /**
     * 交易类型,01:注册赠送,02:充值,03:提现,04:投注,05:盈利
     */
    private String tradeType;

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

}
