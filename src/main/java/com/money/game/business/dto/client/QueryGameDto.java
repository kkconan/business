package com.money.game.business.dto.client;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author conan
 *         2018/1/10 15:15
 **/
@Data
public class QueryGameDto implements Serializable{

    private static final long serialVersionUID = -871481139532975107L;

    /**
     * 查询总数
     */
    private Integer pageSize = 50;

    /**
     * 查询页码
     */
    private Integer currentPage = 1;


    @ApiModelProperty(value = "比赛类型 01:球赛 02:猜涨跌")
    private String type;

}
