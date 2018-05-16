package com.money.game.business.biz;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.money.game.business.constant.DictEnum;
import com.money.game.business.constant.ErrorEnum;
import com.money.game.business.dto.client.QueryGameDto;
import com.money.game.business.dto.client.QueryOrderDto;
import com.money.game.business.entity.*;
import com.money.game.business.exception.BizException;
import com.money.game.business.service.*;
import com.money.game.business.vo.client.GameEntityVo;
import com.money.game.business.vo.client.MarketDetailVo;
import com.money.game.business.vo.client.MarketInfoVo;
import com.money.game.core.constant.ResponseData;
import com.money.game.core.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author conan
 *         2018/1/5 16:21
 **/
@Component
@Slf4j
public class GameBiz {

    @Value("${huobi.pro.api.url:https://api.huobi.pro/market/history/kline}")
    private String proMarketApi;

    @Value("${game.time.frequency:30}")
    private Integer timeFrequency;

    @Value("${game.play.frequency:10}")
    private Integer playFrequency;


    @Value("${game.commission:10}")
    private Integer commission;


    @Autowired
    private GameService gameService;

    @Autowired
    private GamePropertiesService gamePropertiesService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private SmartContractBiz smartContractBiz;

    /**
     * 游戏开始
     */
    public void startDigitalCashGame() {
        GameEntity gameEntity = gameService.findByStartTime(DateUtil.getCurrDateMmss(), DictEnum.GAME_STATUS_01.getCode());
        if (gameEntity != null) {
            MarketDetailVo marketDetailVo = getMarketDetail();
            if (marketDetailVo.getId().equals(String.valueOf(gameEntity.getStartTime().getTime()).substring(0, 10))) {
                log.info("game start... id={}", gameEntity.getOid());
                gameEntity.setStatus(DictEnum.GAME_STATUS_02.getCode());
                gameEntity.setOpenAmount(marketDetailVo.getOpen());
                smartContractBiz.gameStart(gameEntity, marketDetailVo.getOpen());
                gameService.save(gameEntity);
            }
        }
    }


    public void endDigitalCashGame() {
        Date currentDate = DateUtil.getCurrDateMmss();
        GameEntity gameEntity = gameService.findByEndTime(currentDate, DictEnum.GAME_STATUS_02.getCode());
        if (gameEntity != null) {
            log.info("game end... id={}", gameEntity.getOid());
            Long endLongTime = gameEntity.getEndTime().getTime();
            //结束时间的前一分钟,市场行情实际是获取结束时间的前一分钟行情
            Date lastEndTime = DateUtil.addMinute(gameEntity.getEndTime(), -1);
            //当前时间和游戏结束时间分钟差,05:00结束,05:00 查询,结果间隔1次
            int time = new BigDecimal(currentDate.getTime() - endLongTime + 1).divide(new BigDecimal(60), 0, BigDecimal.ROUND_UP).intValue();
            if (time > 2000) {
                log.warn("超时未完成,状态异常,结果作废,time={}", time);
                gameEntity.setStatus(DictEnum.GAME_STATUS_04.getCode());
                gameEntity.setResult(DictEnum.GAME_PROPERTIES_EXEC.getCode());
                gameService.save(gameEntity);
                return;
            }
            //获取前一分钟的收盘价格
            MarketInfoVo marketInfoVo = getMarketInfo(DictEnum.MARKEY_PERIOD_1MIN.getCode(), time + 1, DictEnum.MARKEY_SYMBOL_BTCUSDT.getCode());

            List<MarketDetailVo> marketDetailVoList = marketInfoVo.getData();
            for (MarketDetailVo marketDetailVo : marketDetailVoList) {
                //获取游戏结束时分k收盘价格
                if (marketDetailVo.getId().equals(String.valueOf(lastEndTime.getTime()).substring(0, 10))) {
                    log.info("get market close amount... marketDetailVo={}", marketDetailVo);
                    gameEntity.setCloseAmount(marketDetailVo.getClose());
                    gameEntity.setStatus(DictEnum.GAME_STATUS_03.getCode());

                    if (gameEntity.getCloseAmount().compareTo(gameEntity.getOpenAmount()) > 0) {
                        gameEntity.setResult(DictEnum.GAME_PROPERTIES_WIN.getCode());
                    } else if (gameEntity.getCloseAmount().compareTo(gameEntity.getOpenAmount()) == 0) {
                        gameEntity.setResult(DictEnum.GAME_PROPERTIES_DRAW.getCode());
                    } else {
                        gameEntity.setResult(DictEnum.GAME_PROPERTIES_LOSE.getCode());
                    }

                    //游戏结束
                    smartContractBiz.gameEnd(gameEntity, marketDetailVo.getOpen());

                    gameService.save(gameEntity);
                    //处理结果
                    this.calcResult(gameEntity);
                    break;
                }
            }
        }
    }


    /**
     * 初始化游戏次数
     */
    public void initDigitalCashGame() {
        //一天的总次数
        int time = 24 * 60 / timeFrequency;
        Date tomorrow = DateUtil.addDay(DateUtil.getCurrDate(), 0);
        Date initDate = DateUtil.getTodayZero(tomorrow);
        Date startTime;
        Date endTime;
        String code;
        int count;
        GameEntity lastGameEntity = gameService.findLastRecordByType(DictEnum.GAME_TYPE_02.getCode());
        if (lastGameEntity != null && lastGameEntity.getCreateTime().after(DateUtil.getCurrDate())) {
            log.info("today game is already create!,lastGameEntity createTime={}", lastGameEntity.getCreateTime());
        } else {
            for (int i = 0; i < time; i++) {
                count = i + 1;
                startTime = DateUtil.addMinute(initDate, i * timeFrequency);
                endTime = DateUtil.addMinute(startTime, playFrequency);
                code = DateUtil.getCurrentDate() + count;
                GameEntity gameEntity = gameService.saveDigitalCashGame(startTime, endTime, code, count);
                gamePropertiesService.saveDigitalCashGamePropeties(gameEntity.getOid());
            }
        }
    }

    public void createSmartGame() {
        GameEntity gameEntity = gameService.findByDoConut(DictEnum.GAME_TYPE_02.getCode(), 0);
        if (gameEntity != null) {
            //创建合约游戏
            smartContractBiz.createGame(gameEntity);
            gameEntity.setDoCount(gameEntity.getDoCount() + 1);
            gameService.save(gameEntity);
        }
    }


    public ResponseData findAllEffectGame(QueryGameDto dto) {

        Page<GameEntity> page = gameService.findAllEffectGame(dto);
        List<GameEntity> gameList = page.getContent();
        List<GameEntityVo> gameEntityVoList = new ArrayList<>();
        for (GameEntity game : gameList) {
            GameEntityVo vo = new GameEntityVo();
            BeanUtils.copyProperties(game, vo);
            List<GamePropertiesEntity> gamePropertiesEntityList = gamePropertiesService.findByGameId(game.getOid());
            vo.setGamePropertiesList(gamePropertiesEntityList);
            gameEntityVoList.add(vo);
        }
        return ResponseData.success(gameEntityVoList, dto.getCurrentPage(), dto.getPageSize(), page.getTotalElements());
    }

    /**
     * (fundingPool-proValue)*90%+proValue=实际总收入
     */
    private void calcResult(GameEntity gameEntity) {
        GamePropertiesEntity gamePropertiesEntity = gamePropertiesService.findByGameIdAndProKey(gameEntity.getOid(), gameEntity.getResult());
        QueryOrderDto dto = new QueryOrderDto();
        dto.setGameId(gameEntity.getOid());
        dto.setKey(gameEntity.getResult());
        Page<OrderEntity> page;
        do {
            page = orderService.findByGameIdAndProKey(dto);
            dto.setCurrentPage(dto.getCurrentPage() + 1);
            List<OrderEntity> orderList = page.getContent();
            recordResult(orderList, gamePropertiesEntity.getAmount(), gameEntity);
        } while (page.getTotalPages() > dto.getPageSize());
    }

    /**
     * 分发结果
     *
     * @param operationAmount 获胜方投注总额
     */
    private void recordResult(List<OrderEntity> orderList, BigDecimal operationAmount, GameEntity gameEntity) {
        //总收入额(去除手续费之后可分摊的总额 (总投注额-胜利方投注额)*0.9)
        BigDecimal totalAmount = (gameEntity.getFundingPool().subtract(operationAmount)).multiply(new BigDecimal(100 - commission).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
        //平台收入
        BigDecimal platFormAmount = gameEntity.getFundingPool().subtract(operationAmount).subtract(totalAmount);
        log.info("处理结果,totalAmount={},operationAmount={},gameId={}", totalAmount, operationAmount, gameEntity.getOid());
        //用户获得的总数
        BigDecimal userOrderAmount = BigDecimal.ZERO;
        for (OrderEntity orderEntity : orderList) {
            //总收入额*用户下注额/获胜方总投注额=用户获得的收益
            BigDecimal orderAmount = totalAmount.multiply(orderEntity.getAmount()).divide(operationAmount, 2, BigDecimal.ROUND_DOWN);
            userOrderAmount = userOrderAmount.add(operationAmount);
            //更新余额
            AccountEntity accountEntity = accountService.findById(orderEntity.getAccountOid());
            accountEntity.setBalance(accountEntity.getBalance().add(orderAmount));
            accountService.save(accountEntity);
            //交易记录
            PaymentEntity payment = paymentService.initPayment(orderEntity.getAccountOid(), orderEntity.getUserOid(), DictEnum.TRADE_TYPE_05.getCode(), orderEntity.getOrderNo(),
                    DictEnum.PAY_STATUS_03.getCode(), DictEnum.PAY_DIRECTION_01.getCode(), orderAmount);
            payment.setGameId(orderEntity.getGameId());
            paymentService.save(payment);
        }
        //更新订单状态
        orderService.updateStatusByGameId(DictEnum.ORDER_STATUS_02.getCode(), gameEntity.getOid());
        //更新平台账户余额
        AccountEntity accountEntity = accountService.findByAccountType(DictEnum.ACCOUNT_TYPE_05.getCode());
        if (accountEntity != null) {
            log.info("更新平台账户余额,oid={}", accountEntity.getOid());
            accountEntity.setBalance(accountEntity.getBalance().add(platFormAmount));
            accountService.save(accountEntity);
            //交易记录
            PaymentEntity payment = paymentService.initPayment(accountEntity.getOid(), accountEntity.getUserOid(), DictEnum.TRADE_TYPE_05.getCode(), null,
                    DictEnum.PAY_STATUS_03.getCode(), DictEnum.PAY_DIRECTION_01.getCode(), platFormAmount);
            payment.setGameId(gameEntity.getOid());
            paymentService.save(payment);
        }

    }

    private MarketDetailVo getMarketDetail() {
        MarketInfoVo marketInfoVo = getMarketInfo(DictEnum.MARKEY_PERIOD_1MIN.getCode(), 1, DictEnum.MARKEY_SYMBOL_BTCUSDT.getCode());
        return marketInfoVo.getData().get(0);
    }

    /**
     * @param period K线类型 1min, 5min, 15min, 30min, 60min, 1day, 1mon, 1week, 1year
     * @param size   获取数量 [1,2000]
     * @param symbol 交易对 btcusdt, bccbtc, rcneth
     */
    private MarketInfoVo getMarketInfo(String period, Integer size, String symbol) {
        String jsonStr = null;
        try {
            CloseableHttpClient httpclient = HttpClientBuilder.create().build();
            //超过2000分钟，默认查询100条
            if (size > 2000) {
                size = 2000;
            }
            String url = proMarketApi + "?period=" + period + "&size=" + size + "&symbol=" + symbol;
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            jsonStr = EntityUtils.toString(entity, "utf-8");
            httpGet.releaseConnection();
            MarketInfoVo marketInfoVo = new ObjectMapper().readValue(jsonStr, MarketInfoVo.class);
            if (marketInfoVo.getData() == null || marketInfoVo.getData().isEmpty()) {
                throw new BizException(ErrorEnum.MARKEY_INFO_FAIL);
            }
            log.info("marketInfoVo={}", marketInfoVo);
            return marketInfoVo;
        } catch (IOException e) {
            log.error("获取行情失败,e={},result={}", e.getMessage(), e, jsonStr);
        }
        throw new BizException(ErrorEnum.MARKEY_INFO_FAIL);
    }
}
