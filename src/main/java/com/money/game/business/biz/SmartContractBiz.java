package com.money.game.business.biz;

import com.alibaba.fastjson.JSONObject;
import com.money.game.business.entity.GameEntity;
import com.money.game.business.util.HttpUtilManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URLEncoder;

/**
 * @author conan
 *         2018/5/12 15:01
 **/
@Component
@Slf4j
public class SmartContractBiz {

    @Value("${node.server.url:http://localhost:8888/callFunc?func=}")
    private  String nodeServerUrl;

    public void createGame(GameEntity game) {
        try {
            log.info("createGame to node.");
            JSONObject contractJson = new JSONObject();
            contractJson.put("id",game.getCode());
            contractJson.put("start",game.getStartTime().getTime());
            contractJson.put("end",game.getEndTime().getTime());
            String result = initJsonParam(contractJson,"createGame");
            log.info("result={}",result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void gameStart(GameEntity game, BigDecimal open) {
        try {
            log.info("gameStart to node.");
            JSONObject contractJson = new JSONObject();
            contractJson.put("id",game.getCode());
            contractJson.put("index",open);
            String result = initJsonParam(contractJson,"gameStart");
            log.info("result={}",result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gameEnd(GameEntity game,BigDecimal close) {
        try {
            log.info("gameEnd to node.");
            JSONObject contractJson = new JSONObject();
            contractJson.put("id",game.getCode());
            contractJson.put("index",close);
            String result = initJsonParam(contractJson,"gameEnd");
            log.info("result={}",result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String initJsonParam(JSONObject contractJson,String mothod) {
        String result = null;
        try {
            String url = nodeServerUrl+mothod+"&&arg=" +URLEncoder.encode(contractJson.toJSONString());
            result = HttpUtilManager.getInstance().requestHttpGet(url,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
