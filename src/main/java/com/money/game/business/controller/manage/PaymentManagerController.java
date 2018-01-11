package com.money.game.business.controller.manage;

import com.money.game.business.biz.PaymentBiz;
import com.money.game.business.controller.BaseController;
import com.money.game.business.dto.manager.PaymentQueryDto;
import com.money.game.business.dto.manager.PaymentVerifyDto;
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
@Api(value = "order", description = "后台管理交易接口API")
@RequestMapping(value = "/api/manager/payment", produces = "application/json;charset=UTF-8")
public class PaymentManagerController extends BaseController {

    @Autowired
    private PaymentBiz paymentBiz;


    /**
     * 列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation(value = "列表", notes = "", httpMethod = "POST")
    @ApiImplicitParams({@ApiImplicitParam(name = "dto", value = "列表参数", required = true, paramType = "body", dataType = "PaymentQueryDto")})
    @ResponseBody
    public ResponseData list(@Valid @RequestBody PaymentQueryDto dto) {
        this.getLoginUser();
        return paymentBiz.list(dto);
    }

    /**
     * 审核
     */
    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    @ApiOperation(value = "审核", notes = "", httpMethod = "POST")
    @ApiImplicitParams({@ApiImplicitParam(name = "dto", value = "审核参数", required = true, paramType = "body", dataType = "PaymentVerifyDto")})
    @ResponseBody
    public ResponseData verify(@Valid @RequestBody PaymentVerifyDto dto) {
        this.getLoginUser();
        return paymentBiz.verify(dto);
    }

}
