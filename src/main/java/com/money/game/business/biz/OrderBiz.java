package com.money.game.business.biz;

import com.money.game.business.constant.DictEnum;
import com.money.game.business.constant.ErrorEnum;
import com.money.game.business.dto.client.OrderDto;
import com.money.game.business.entity.*;
import com.money.game.business.exception.BizException;
import com.money.game.business.service.*;
import com.money.game.business.vo.client.CreateOrderVo;
import com.money.game.core.constant.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author conan
 *         2018/1/8 14:27
 **/
@Component
@Slf4j
public class OrderBiz {

    @Autowired
    private GameService gameService;

    @Autowired
    private GamePropertiesService gamePropertiesService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PaymentService paymentService;

    public ResponseData createOrder(OrderDto dto) {
        log.info("创建订单,dto={}", dto);
        AccountEntity accountEntity = accountService.findByUserOid(dto.getUserOid());
        if (accountEntity.getBalance().compareTo(dto.getAmount()) < 0) {
            throw new BizException(ErrorEnum.ACCOUNT_BALANCE_TOO_LITTLE);
        }
        GameEntity gameEntity = gameService.findByOid(dto.getGameId());

        if (!DictEnum.GAME_STATUS_01.getCode().equals(gameEntity.getStatus())) {
            throw new BizException(ErrorEnum.GAME_START);
        }
        CreateOrderVo vo = doOrder(dto, accountEntity, gameEntity);
        log.info("创建订单结束,vo={}", vo);
        return ResponseData.success(vo);
    }

    private CreateOrderVo doOrder(OrderDto dto, AccountEntity accountEntity, GameEntity gameEntity) {

        //创建订单
        OrderEntity orderEntity = orderService.initOrder(accountEntity.getOid(), dto.getUserOid(), dto.getGameId(), dto.getAmount(), dto.getOperation());
        orderService.save(orderEntity);
        //增加资金池
        gameEntity.setFundingPool(gameEntity.getFundingPool().add(dto.getAmount()));
        gameService.save(gameEntity);
        //计算赔率
        CreateOrderVo createOrderVo = calcProbability(dto, gameEntity);
        //增加交易明细
        PaymentEntity paymentEntity = paymentService.initPayment(accountEntity.getOid(), dto.getUserOid(), DictEnum.TRADE_TYPE_04.getCode(),
                orderEntity.getOrderNo(), DictEnum.PAY_STATUS_03.getCode(), DictEnum.PAY_DIRECTION_02.getCode(), dto.getAmount());
        paymentEntity.setGameId(gameEntity.getOid());
        paymentService.save(paymentEntity);
        //扣除余额
        accountEntity.setBalance(accountEntity.getBalance().subtract(dto.getAmount()));
        accountService.save(accountEntity);
        return createOrderVo;
    }

    /**
     * 计算概率
     */
    private CreateOrderVo calcProbability(OrderDto dto, GameEntity gameEntity) {

        CreateOrderVo createOrderVo = new CreateOrderVo();
        List<GamePropertiesEntity> gamePropertiesList = gamePropertiesService.findByGameId(gameEntity.getOid());
        for (GamePropertiesEntity gameProperties : gamePropertiesList) {
            if (gameProperties.getProKey().equals(dto.getOperation())) {
                gameProperties.setSupportCount(gameProperties.getSupportCount() + 1);
                gameProperties.setAmount(gameProperties.getAmount().add(dto.getAmount()));
            }
            gameProperties.setProValue(gameProperties.getAmount().divide(gameEntity.getFundingPool(), 2, BigDecimal.ROUND_HALF_DOWN));
            gamePropertiesService.save(gameProperties);
        }
        createOrderVo.setGamePropertiesList(gamePropertiesList);
        createOrderVo.setGameId(dto.getGameId());

        return createOrderVo;
    }
}
