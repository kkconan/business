package com.money.game.business.controller.client;

import com.money.game.business.biz.OrderBiz;
import com.money.game.business.controller.BaseController;
import com.money.game.business.dto.client.OrderDto;
import com.money.game.core.constant.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author conan
 *         2018/1/8 17:27
 **/

@RestController
@Api(value = "order", description = "订单接口API")
@RequestMapping(value = "/api/client/order", produces = "application/json;charset=UTF-8")
public class OrderController extends BaseController{

    @Autowired
    private OrderBiz orderBiz;

    /**
     * 创建订单
     */
    @RequestMapping(value = "/createOrder", method = RequestMethod.POST)
    @ApiOperation(value = "创建订单", notes = "", httpMethod = "POST")
    @ApiImplicitParams({@ApiImplicitParam(name = "dto", value = "订单参数", required = true, paramType = "body", dataType = "OrderDto")})
    @ResponseBody
    public ResponseData createOrder(@Valid @RequestBody OrderDto dto) {
        String userId = this.getLoginUser();
        return orderBiz.createOrder(userId,dto);
    }
}
