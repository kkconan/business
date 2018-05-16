package com.money.game.business.service;

import com.money.game.business.dto.client.QueryGameDto;
import com.money.game.business.entity.GameEntity;
import org.springframework.data.domain.Page;

import java.util.Date;

/**
 * @author conan
 *         2018/1/5 16:02
 **/
public interface GameService {

    /**
     * 初始化
     */
    GameEntity saveDigitalCashGame(Date startTime, Date endTime, String code, Integer count);

    GameEntity save(GameEntity game);

    /**
     * 查询最新一条开始时间小于等于当前时间未开始的记录
     */
    GameEntity findByStartTime(Date startTime, String status);

    /**
     * * 查询最新一条结束时间小于等于当前时间进行中的记录
     */
    GameEntity findByEndTime(Date endTime, String status);

    GameEntity findByOid(String oid);

    /**
     * 查询指定类型的最新一条记录
     */
    GameEntity findLastRecordByType(String type);

    /**
     * 分页查询有效的记录(进行中和未开始)
     */
    Page<GameEntity> findAllEffectGame(QueryGameDto dto);

    /**
     * 查询指定类型的最新一条记录
     */
    GameEntity findByDoConut(String type,Integer doCount);
}
