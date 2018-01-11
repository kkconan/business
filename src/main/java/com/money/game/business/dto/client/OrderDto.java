package com.money.game.business.dto.client;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author conan
 *         2018/1/8 14:30
 **/
@Data
public class OrderDto implements Serializable {
    private static final long serialVersionUID = 6710397738021682209L;

    @ApiModelProperty(value = "用户id")
    private String userOid;

    @ApiModelProperty(value = "订单金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "游戏id")
    private String gameId;

    /**
     * 胜:win 负:lose 平:draw
     */
    @ApiModelProperty(value = "投注选项（胜:win 负:lose 平:draw）")
    private String operation;

}
