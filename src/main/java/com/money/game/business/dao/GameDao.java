package com.money.game.business.dao;

import com.money.game.business.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * @author conan
 *         2017/10/26 13:41
 **/
public interface GameDao extends JpaRepository<GameEntity, String>, JpaSpecificationExecutor<GameEntity> {

    @Query(value = "select * from T_GAME where   startTime <= ?1 and endTime >= ?1 and status = ?2  order by startTime desc limit 1 ", nativeQuery = true)
    GameEntity findByStartTime(Date startTime, String status);


    @Query(value = "select * from T_GAME where   endTime <= ?1 and status = ?2  order by endTime desc limit 1 ", nativeQuery = true)
    GameEntity findByEndTime(Date endTime, String status);

    @Query(value = "select * from T_GAME where   type = ?1  order by createTime desc limit 1 ", nativeQuery = true)
    GameEntity findLastRecordByType(String type);

    @Query(value = "select * from T_GAME where   type = ?1 and doCount <= ?2 order by startTime asc limit 1 ", nativeQuery = true)
    GameEntity findByDoConut(String type ,Integer doCount);


}


