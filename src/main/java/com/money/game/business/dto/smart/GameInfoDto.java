package com.money.game.business.dto.smart;

import com.sun.jmx.snmp.Timestamp;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author conan
 *         2018/5/12 15:07
 **/
@Data
public class GameInfoDto
{
    /**
     * 开盘价
     */
    private BigDecimal startIndex;

    /**
     * 收盘价
     */
    private BigDecimal endIndex;

    /**
     * 押涨人数
     */
    private Integer upNum;

    /**
     * 押涨总金额
     */
    private BigDecimal upTotalMoney;

    /**
     * 压跌人数
     */
    private Integer downNum;

    private BigDecimal downTotalMoney;

    /**
     * 压平人数
     */
    private Integer drawNum;

    /**
     * 压平总金额
     */
    private BigDecimal drawTotalMoney;

    private Timestamp startTime;

    private  Timestamp endTime;

    /**
     * 状态
     */
    private String state;


}
