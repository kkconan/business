package com.money.game.business.schedule;

import com.money.game.business.biz.GameBiz;
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

    @Scheduled(cron = "${cron.option[digitalCash.game.end]:0/5 * * * * ?}")
    public void endDigitalCashGame() {
        gameBiz.endDigitalCashGame();
    }


}
