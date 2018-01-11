package com.money.game.business.dao;

import com.money.game.business.entity.GamePropertiesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author conan
 *         2017/10/26 13:41
 **/
public interface GamePropertiesDao extends JpaRepository<GamePropertiesEntity, String>, JpaSpecificationExecutor<GamePropertiesEntity> {

    List<GamePropertiesEntity>  findByGameId(String gameId);

    GamePropertiesEntity findByGameIdAndProKey(String gameId, String key);

}


