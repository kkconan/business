package com.money.game.business.service;

import com.money.game.business.dto.client.QueryOrderDto;
import com.money.game.business.entity.OrderEntity;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

/**
 * @author conan
 *         2018/1/8 14:03
 **/
public interface OrderService {

    OrderEntity initOrder(String accountOid, String userOid, String gameId, BigDecimal amount, String key);

    OrderEntity save(OrderEntity orderEntity);

    Page<OrderEntity> findByGameIdAndProKey(QueryOrderDto dto);

    Integer updateStatusByGameId(String status,String gameId);
}
