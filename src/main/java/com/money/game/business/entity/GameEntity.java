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
import java.util.Date;

/**
 * 赛事
 * @author conan
 *         2018/1/4 17:24
 **/
@Entity
@Table(name = "T_GAME")
@lombok.Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class GameEntity extends UUID implements Serializable{

    private static final long serialVersionUID = -7058115224860151015L;

    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 场次
     */
    private Integer count;

    /**
     * 比赛开始时间
     */
    private Date startTime;


    /**
     * 比赛结束时间
     */
    private Date endTime;

    /**
     *比赛类型 01:球赛 02:猜涨跌
     */
    private String type;

    /**
     * 状态 01:未开始,02:进行中,03:已结束
     */
    private String status;

    /**
     * 资金池
     */
    private BigDecimal fundingPool = BigDecimal.ZERO;

    /**
     * 开盘价
     */
    private BigDecimal openAmount = BigDecimal.ZERO;

    /**
     * 闭盘价
     */
    private BigDecimal closeAmount = BigDecimal.ZERO;

    /**
     * 比赛结果 win:胜(涨) lose:负(跌) draw:平
     */
    private String result;

    /**
     * 已处理到合约的次数
     */
    private Integer doCount = 0;

    private Timestamp updateTime;

    private Timestamp createTime;
}
