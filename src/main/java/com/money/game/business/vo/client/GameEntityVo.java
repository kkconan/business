package com.money.game.business.vo.client;

import com.money.game.business.entity.GamePropertiesEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @author conan
 *         2018/1/10 15:34
 **/
@Data
public class GameEntityVo implements Serializable {

    private static final long serialVersionUID = -3133765704529387638L;

    private String oid;

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
     * 比赛类型 01:球赛 02:猜涨跌
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

    private Timestamp createTime;

    private List<GamePropertiesEntity> gamePropertiesList;


}
