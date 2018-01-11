package com.money.game.business.dao;

import com.money.game.business.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
 * @author conan
 *         2017/10/26 13:41
 **/
public interface OrderDao extends JpaRepository<OrderEntity, String>, JpaSpecificationExecutor<OrderEntity> {


    @Query(value = "update T_ORDER set status =?1 where gameId = ?2", nativeQuery = true)
    @Modifying
    @Transactional
    Integer updateStatusByGameId(String status, String gameId);
}


