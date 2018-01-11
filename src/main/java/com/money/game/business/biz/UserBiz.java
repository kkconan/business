package com.money.game.business.biz;

import com.money.game.business.constant.DictEnum;
import com.money.game.business.constant.ErrorEnum;
import com.money.game.business.dto.client.RegisterDto;
import com.money.game.business.dto.client.UserLoginDto;
import com.money.game.business.entity.AccountEntity;
import com.money.game.business.entity.PaymentEntity;
import com.money.game.business.entity.UserEntity;
import com.money.game.business.exception.BizException;
import com.money.game.business.service.AccountService;
import com.money.game.business.service.PaymentService;
import com.money.game.business.service.UserService;
import com.money.game.core.util.CheckUtil;
import com.money.game.core.util.PwdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author conan
 *         2018/1/5 14:27
 **/
@Component
@Slf4j
public class UserBiz {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PaymentService paymentService;

    @Value("${register.send.amount:888}")
    private BigDecimal registerSendAmount;


    /**
     * 注册
     */
    public UserEntity register(RegisterDto dto) {
        log.info("注册开始,dto={}", dto);
        //检查手机号是否合法
        CheckUtil.isMobileNO(dto.getPhone(), false, ErrorEnum.USER_REGISTER_PHONE_IS_NULL.getCode(), ErrorEnum.USER_REGISTER_PHONE_CHECK_FAIL.getCode());
        //检查密码是否符合规则
        CheckUtil.checkloginPwd(dto.getUserPwd(), 6, 16, false, ErrorEnum.USER_REGISTER_PWD_IS_NULL.getCode(), ErrorEnum.USER_REGISTER_PWD_CHECK_FAIL.getCode());
        //创建用户账户
        UserEntity user = userService.addUser(dto);
        //创建用户资金账户
        AccountEntity account = accountService.initAccount(user.getOid(), DictEnum.USER_TYPE_01.getCode());
        account.setBalance(registerSendAmount);
        accountService.save(account);
        //增加交易记录
        PaymentEntity payment = paymentService.initPayment(account.getOid(), user.getOid(), DictEnum.TRADE_TYPE_01.getCode(), null,
                DictEnum.PAY_STATUS_03.getCode(), DictEnum.PAY_DIRECTION_01.getCode(), registerSendAmount);
        paymentService.save(payment);
        log.info("注册结束,user={}", user);
        return user;
    }


    /**
     * 登录
     */
    public String login(UserLoginDto dto) {
        log.info("用户登录dto={}", dto);
        UserEntity user = userService.findByUserAccountOrPhone(dto.getUserAccount(), dto.getUserAccount());
        //校验密码
        boolean result = PwdUtil.checkPassword(dto.getUserPwd(), user.getUserPwd(), user.getSalt());
        if (!result) {
            throw new BizException(ErrorEnum.USER_PWD_FAIL);
        }
        log.info("登录成功,userOid={}", user.getOid());
        return user.getOid();
    }
}
