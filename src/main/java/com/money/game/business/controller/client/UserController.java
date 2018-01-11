package com.money.game.business.controller.client;

import com.money.game.business.biz.UserBiz;
import com.money.game.business.controller.BaseController;
import com.money.game.business.dto.client.RegisterDto;
import com.money.game.business.dto.client.UserLoginDto;
import com.money.game.business.entity.UserEntity;
import com.money.game.core.captcha.CaptchaUtil;
import com.money.game.core.constant.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author conan
 *         2018/1/5 10:31
 **/
@RestController
@RequestMapping(value = "/api/client/user", produces = "application/json")
@Api(value = "user", description = "用户接口API")
@Slf4j
public class UserController extends BaseController {

    @Autowired
    private CaptchaUtil captchaUtil;

    @Autowired
    private UserBiz userBiz;

    /**
     * 注册
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ApiOperation(value = "注册", notes = "", httpMethod = "POST")
    @ApiImplicitParams({@ApiImplicitParam(name = "dto", value = "注册参数", required = true, paramType = "body", dataType = "RegisterDto")})
    @ResponseBody
    public ResponseData regist(@Valid @RequestBody RegisterDto dto) {
        // 校验图形验证码
        if (!StringUtils.isEmpty(dto.getImgvc())) {
            this.captchaUtil.validImgVc(dto.getImgvc(), super.session.getId());
        }
        UserEntity user = userBiz.register(dto);
        // 登陆
        super.setLoginUser(user.getOid());
        return ResponseData.success();
    }

    /**
     * 登录
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation(value = "登录", notes = "", httpMethod = "POST")
    @ApiImplicitParams({@ApiImplicitParam(name = "dto", value = "登录参数", required = true, paramType = "body", dataType = "UserLoginDto")})
    @ResponseBody
    public ResponseData login(@Valid @RequestBody UserLoginDto dto) {

        String userOid = userBiz.login(dto);
        super.setLoginUser(userOid);
        return ResponseData.success(userOid);
    }
}
