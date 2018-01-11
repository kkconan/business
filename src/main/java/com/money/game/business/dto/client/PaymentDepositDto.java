package com.money.game.business.dto.client;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author conan
 *         2018/1/11 14:16
 **/
@Data
public class PaymentDepositDto implements Serializable{
    private static final long serialVersionUID = -8767514548165559933L;

    @ApiModelProperty(value = "充值金额")
    private BigDecimal amount;
}
