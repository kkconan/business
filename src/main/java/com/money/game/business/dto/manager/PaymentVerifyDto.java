package com.money.game.business.dto.manager;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author conan
 *         2018/1/11 14:16
 **/
@Data
public class PaymentVerifyDto implements Serializable{
    private static final long serialVersionUID = -8767514548165559933L;

    @ApiModelProperty(value = "审核状态 02:审核通过,03:审核拒绝")
    private String auditStatus;

    @ApiModelProperty(value = "审核意见")
    private String remark;

    @ApiModelProperty(value = "交易流水")
    private String payNo;

}
