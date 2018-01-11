package com.money.game.business.controller.client;

import com.money.game.business.controller.BaseController;
import com.money.game.core.captcha.CaptchaUtil;
import com.money.game.core.captcha.CaptchaValidReq;
import com.money.game.core.web.view.BaseResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/client/captcha", produces = "application/json;charset=UTF-8")
public class CaptchaClientController extends BaseController {

    @Autowired
    private CaptchaUtil captchaUtil;

    /**
     * 获取图形验证码
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "getimgvc")
    public ModelAndView getKaptchaImage(HttpServletRequest request,
                                        HttpServletResponse response) throws Exception {
        String sessionId = super.session.getId();
        return captchaUtil.getImgVc(request, response, sessionId);
    }

    /**
     * 校验图形验证码
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "checkimgvc", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<BaseResp> getKaptchaImage(@Valid @RequestBody CaptchaValidReq req) {
        req.setSessionId(super.session.getId());
        BaseResp rep = captchaUtil.checkImgVc(req);
        return new ResponseEntity<BaseResp>(rep, HttpStatus.OK);
    }

}
