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
 * 比赛属性
 *
 * @author conan
 *         2018/1/4 17:54
 **/
@Entity
@Table(name = "T_GAME_PROPERTIES")
@lombok.Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class GamePropertiesEntity extends UUID implements Serializable {

    private static final long serialVersionUID = 2890589316577104637L;

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


    private Timestamp updateTime;

    private Timestamp createTime;

}
