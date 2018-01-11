package com.money.game.business.vo.client;

import com.money.game.business.entity.GamePropertiesEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author conan
 *         2018/1/10 15:36
 **/
@Data
public class GamePropertiesVo extends GamePropertiesEntity implements Serializable{

    private static final long serialVersionUID = -2088896044615818729L;

    private String oid;

    private String gameId;

    /**
     * proKey 值 胜:win 负:lose 平:draw
     */
    private String proKey;

    /**
     * 赔率
     */
    private BigDecimal proValue = BigDecimal.ZERO;

    /**
     * 投注额
     */
    private BigDecimal amount = BigDecimal.ZERO;

    /**
     * 支持人数
     */
    private Integer supportCount = 0;


    private Timestamp createTime;
}
