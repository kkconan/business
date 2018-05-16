package com.money.game.business.schedule;

import com.money.game.business.biz.GameBiz;
import com.money.game.business.biz.SmartContractBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author conan
 *         2018/1/9 17:58
 **/
@Component
@Slf4j
public class GameSchedule {

    @Autowired
    private GameBiz gameBiz;

    @Autowired
    private SmartContractBiz smartContractBiz;

    /**
     *
     */
    @Scheduled(cron = "${cron.option[digitalCash.game.init]:0 0 23 * * ?}")
    public void initDigitalCashGame() {
        log.info("init digitalCash game.");
        gameBiz.initDigitalCashGame();

    }

    @Scheduled(cron = "${cron.option[digitalCash.game.start]:0/5 * * * * ?}")
    public void startDigitalCashGame() {
        gameBiz.startDigitalCashGame();
    }

    @Scheduled(cron = "${cron.option[digitalCash.game.end]:0/6 * * * * ?}")
    public void endDigitalCashGame() {
        gameBiz.endDigitalCashGame();
    }

    @Scheduled(cron = "${cron.option[create.smart.game]:0 0/2 * * * ?}")
    public void createSmartGame() {
        log.info("create smart game.");
        gameBiz.createSmartGame();

    }


}
