package com.money.game.business.controller.client;

import com.money.game.business.biz.PaymentBiz;
import com.money.game.business.controller.BaseController;
import com.money.game.business.dto.client.PaymentDepositDto;
import com.money.game.business.dto.manager.PaymentQueryDto;
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
@Api(value = "order", description = "交易接口API")
@RequestMapping(value = "/api/client/payment", produces = "application/json;charset=UTF-8")
public class PaymentController extends BaseController {

    @Autowired
    private PaymentBiz paymentBiz;

    /**
     * 充值
     */
    @RequestMapping(value = "/deposit", method = RequestMethod.POST)
    @ApiOperation(value = "充值", notes = "", httpMethod = "POST")
    @ApiImplicitParams({@ApiImplicitParam(name = "dto", value = "充值参数", required = true, paramType = "body", dataType = "PaymentDepositDto")})
    @ResponseBody
    public ResponseData deposit(@Valid @RequestBody PaymentDepositDto dto) {
        String userId = this.getLoginUser();
        return paymentBiz.deposit(userId, dto.getAmount());
    }

    /**
     * 提现
     */
    @RequestMapping(value = "/withDraw", method = RequestMethod.POST)
    @ApiOperation(value = "提现", notes = "", httpMethod = "POST")
    @ApiImplicitParams({@ApiImplicitParam(name = "dto", value = "提现参数", required = true, paramType = "body", dataType = "PaymentDepositDto")})
    @ResponseBody
    public ResponseData withDraw(@Valid @RequestBody PaymentDepositDto dto) {
        String userId = this.getLoginUser();
        return paymentBiz.withDraw(userId, dto.getAmount());
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation(value = "列表", notes = "", httpMethod = "POST")
    @ApiImplicitParams({@ApiImplicitParam(name = "dto", value = "列表参数", required = true, paramType = "body", dataType = "PaymentQueryDto")})
    @ResponseBody
    public ResponseData list(@Valid @RequestBody PaymentQueryDto dto) {
        String userid = this.getLoginUser();
        dto.setUserOid(userid);
        return paymentBiz.list(dto);
    }

}
