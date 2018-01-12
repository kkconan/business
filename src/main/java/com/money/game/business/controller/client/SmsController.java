package com.money.game.business.controller.client;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author conan
 *         2018/1/12 10:27
 **/

@RestController
@RequestMapping(value = "/api/client/sms", produces = "application/json")
public class SmsController {

    /**
     * 发送短信
     * @param req
     * @return
     */
    @RequestMapping(value = "/sendCode", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<BaseResp> sendCode(@Valid @RequestBody SMSReq req) {
        // 校验图形验证码
        if (!StringUtil.isEmpty(req.getImgvc())) {
            this.captchaService.validImgVc(req.getImgvc(), super.session.getId());
        }
        this.sMSUtils.sendSMS(req.getPhone(), req.getSmsType(), req.getValues());

        return new ResponseEntity<BaseResp>(new BaseResp(), HttpStatus.OK);
    }
}
