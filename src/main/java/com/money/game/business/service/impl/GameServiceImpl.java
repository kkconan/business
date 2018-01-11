package com.money.game.business.service.impl;

import com.money.game.business.constant.DictEnum;
import com.money.game.business.dao.GameDao;
import com.money.game.business.dto.client.QueryGameDto;
import com.money.game.business.entity.GameEntity;
import com.money.game.business.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author conan
 *         2018/1/5 16:03
 **/
@Service
@Slf4j
public class GameServiceImpl implements GameService {

    @Autowired
    private GameDao gameDao;

    /**
     * 初始化数字类型数据
     */
    @Override
    public GameEntity saveDigitalCashGame(Date startTime, Date endTime, String code, Integer count) {
        GameEntity game = new GameEntity();
        game.setName(DictEnum.DIGITAL_CASH_NAME.getCode());
        game.setCode(code);
        game.setCount(count);
        game.setStartTime(startTime);
        game.setEndTime(endTime);
        game.setType(DictEnum.GAME_TYPE_02.getCode());
        game.setStatus(DictEnum.GAME_STATUS_01.getCode());
        return gameDao.save(game);
    }

    @Override
    public GameEntity save(GameEntity game) {
        return gameDao.save(game);
    }

    @Override
    public GameEntity findByStartTime(Date startTime, String status) {
        return gameDao.findByStartTime(startTime, status);
    }

    @Override
    public GameEntity findByEndTime(Date endTime, String status) {
        return gameDao.findByEndTime(endTime, status);
    }

    @Override
    public GameEntity findByOid(String oid) {
        return gameDao.findOne(oid);
    }

    @Override
    public GameEntity findLastRecordByType(String type) {
        return gameDao.findLastRecordByType(type);
    }

    @Override
    public Page<GameEntity> findAllEffectGame(QueryGameDto dto) {
        Pageable pageable = new PageRequest(dto.getCurrentPage() - 1, dto.getPageSize(), new Sort(new Sort.Order(Sort.Direction.ASC, "createTime")));
        return gameDao.findAll(buildSpecification(dto), pageable);
    }


    private Specification<GameEntity> buildSpecification(QueryGameDto dto) {
        Specification<GameEntity> spec = (root, query, cb) -> {
            List<Predicate> bigList = new ArrayList<Predicate>();
            bigList.add(cb.or(cb.equal(root.get("status").as(String.class), DictEnum.GAME_STATUS_01.getCode()), cb.equal(root.get("status").as(String.class), DictEnum.GAME_STATUS_02.getCode())));
            bigList.add(cb.equal(root.get("type").as(String.class), dto.getType()));
            bigList.add(cb.greaterThanOrEqualTo(root.get("endTime").as(Timestamp.class), new Timestamp(new Date().getTime())));

            query.where(cb.and(bigList.toArray(new Predicate[bigList.size()])));
            // 条件查询
            return query.getRestriction();
        };
        return spec;
    }

}
