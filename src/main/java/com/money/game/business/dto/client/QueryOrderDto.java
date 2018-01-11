package com.money.game.business.dto.client;

import lombok.Data;

import java.io.Serializable;

/**
 * @author conan
 *         2018/1/9 15:52
 **/
@Data
public class QueryOrderDto implements Serializable {

    private static final long serialVersionUID = -3159355446108397234L;

    /**
     * 查询总数
     */
    private Integer pageSize = 50;

    /**
     * 查询页码
     */
    private Integer currentPage = 1;

    private String gameId;

    private String key;

}
