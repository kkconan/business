package com.money.game.business.vo.client;

import com.money.game.business.entity.GamePropertiesEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author conan
 *         2018/1/8 17:30
 **/
@Data
public class CreateOrderVo implements Serializable{

    private static final long serialVersionUID = 8109551844962700597L;

    private String gameId;

    List<GamePropertiesEntity> gamePropertiesList;

}
