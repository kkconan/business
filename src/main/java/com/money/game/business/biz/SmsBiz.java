package com.money.game.business.biz;

import com.alibaba.fastjson.JSON;
import com.money.game.basic.component.sms.ContentSms;
import com.money.game.business.constant.DictEnum;
import com.money.game.business.constant.ErrorEnum;
import com.money.game.business.constant.SMSTypeEnum;
import com.money.game.business.exception.BizException;
import com.money.game.business.util.SMSTypeEntity;
import com.money.game.core.util.NumberUtil;
import com.money.game.core.util.StrRedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class SmsBiz {

    @Autowired
    private RedisTemplate<String, String> redis;

    /**
     * 短信验证码过期时间
     */
    private static final int EXPIRE_SECONDS = 120;

    // 短信开关：on发短信/off不发短信
    @Value("${sms.switch:on}")
    private String smsSwitch;

    @Value("${sms.contentTypes:#{null}}")
    private String contentTypes;

    public static final Map<String, String> smsContentsMap = new HashMap<String, String>();

    @PostConstruct
    public void initSMS() {

        if (StringUtils.isNotEmpty(this.contentTypes)) {
            List<SMSTypeEntity> list = JSON.parseArray(this.contentTypes, SMSTypeEntity.class);
            if (smsContentsMap.size() == 0) {
                for (SMSTypeEntity en : list) {
                    smsContentsMap.put(en.getSmsType(), en.getContent());
                }
            }
        }
    }


    /**
     * 根据发送类型发送短信
     *
     * @param phone   手机号
     * @param smsType 短信类型
     * @param values  短信模板值
     */
    public void sendSMSBySendTypes(String phone, String smsType, String[] values) {

        this.generateVeriCode(phone, smsType, values);

//		BaseResp resp = new BaseResp();
        // 发送短信内容
        String notifyContent = "";

        String content = this.replaceComStrArr(smsContentsMap.get(smsType), values);

        if (StringUtils.isNotEmpty(content)) {
            ContentSms contentSms = this.setContentSms(phone, content);
            notifyContent = JSON.toJSONString(contentSms);
            //TODO 发送短信
//            resp = SmsAware.send(contentSms);
        } else {
            // error.define[120006]=无效的短信内容！(CODE:120006)
            throw new BizException(ErrorEnum.SMS_CONTEXT_ERROR);
        }
        // 短信记录日志
//		this.sMSNotifyService.createLog(this.smsSendTypes, notifyContent);

//		if (0 != resp.getErrorCode()) {
//			throw AMPException.getException(resp.getErrorMessage());
//		}
    }

    private ContentSms setContentSms(String phone, String content) {
        ContentSms contentSms = new ContentSms();
        contentSms.setPhone(phone);
        contentSms.setContent(content);
        return contentSms;
    }

    /**
     * 替换掉短信模板中{1}格式的字样
     *
     * @param target 目标
     * @param repArr 替换的值
     */
    private String replaceComStrArr(String target, String[] repArr) {
        if (null == target) {
            return StringUtils.EMPTY;
        }
        for (int i = 1; i <= repArr.length; i++) {
            target = target.replace("{" + i + "}", repArr[i - 1]);
        }
        return target;
    }

    /**
     * 生成短信验证码
     */
    private String[] generateVeriCode(String phone, String smsType, String[] values) {
        if (SMSTypeEnum.smstypeEnum.regist.toString().equals(smsType) ||
                SMSTypeEnum.smstypeEnum.login.toString().equals(smsType) ||
                SMSTypeEnum.smstypeEnum.forgetlogin.toString().equals(smsType) ||
                SMSTypeEnum.smstypeEnum.forgetpaypwd.toString().equals(smsType) ||
                SMSTypeEnum.smstypeEnum.edittradepwd.toString().equals(smsType) ||
                SMSTypeEnum.smstypeEnum.normal.toString().equals(smsType) ||
                SMSTypeEnum.smstypeEnum.activity.toString().equals(smsType)) {
            // 验证码
            values[0] = NumberUtil.randomNumb();

            this.sendVC2Redis(phone, smsType, values[0]);
        }
        return values;
    }

    /**
     * 将验证码保存到redis
     *
     * @param phone    手机号
     * @param smsType  短信类型
     * @param veriCode 验证码
     */
    public void sendVC2Redis(String phone, String smsType, String veriCode) {
        boolean result = StrRedisUtil.setSMSEx(redis, StrRedisUtil.VERI_CODE_REDIS_KEY + phone + "_" + smsType, EXPIRE_SECONDS, veriCode);
        log.info("手机号：{}短信类型为：{}的验证码是：{}", phone, smsType, veriCode);
        if (!result) {
            throw new BizException(ErrorEnum.SMS_CODE_ERROR);
        }
    }


    /**
     * 校验验证码
     *
     * @param phone    手机号
     * @param smsType  短信类型
     * @param veriCode 短信码
     */
    public void checkVeriCode(String phone, String smsType, String veriCode) {
        if (DictEnum.SMS_SWITCH_OFF.getCode().equals(smsSwitch)) {
            log.info("发送短信开关为：off，不进行发送短信，短信类型：{}!", smsType);
            return;
        }
        this.checkPhone(smsType, phone);

        String vericode = StrRedisUtil.get(redis, StrRedisUtil.VERI_CODE_REDIS_KEY + phone + "_" + smsType);
        boolean result = veriCode.equals(vericode);
        if (!result) {
            // error.define[120001]=无效的验证码！(CODE:120001)
            throw new BizException(ErrorEnum.SMS_CODE_CHECK_FAIL);
        }
    }

    public void checkVeriCode(String phone, String veriCode) {
        if (DictEnum.SMS_SWITCH_OFF.getCode().equals(smsSwitch)) {
            log.info("发送短信开关为：off，不进行发送短信，短信类型：{}!", SMSTypeEnum.smstypeEnum.normal.toString());
            return;
        }

        String vericode = StrRedisUtil.get(redis, StrRedisUtil.VERI_CODE_REDIS_KEY + phone + "_" + SMSTypeEnum.smstypeEnum.normal.toString());
        boolean result = veriCode.equals(vericode);
        if (!result) {
            throw new BizException(ErrorEnum.SMS_CODE_CHECK_FAIL);
        }
    }


    /**
     * 校验手机号
     */
    private void checkPhone(String smsType, String phone) {
        // 是否是外部接口访问
        boolean isout = SMSTypeEnum.checkSMSType(smsType);
//		InvestorBaseAccountEntity account = null;
//		if (isout) {
//			account = this.investorBaseAccountService.checkAccount(phone);
//		}
//
//		if (SMSTypeEnum.smstypeEnum.regist.toString().equals(smsType)) {
//
//			switchService.isRegisterAble(phone);
////			switchService.isRegisterAble();
//
//			if (null != account) {
//				// error.define[80020]=该手机号已注册(CODE:80020)
//				throw AMPException.getException(80020);
//			}
//			account = this.investorBaseAccountService.check2SysUserByPhone(phone, true);
//			if (null != account) {
//				// error.define[80045]=您已经注册过了，可直接登录使用系统!(CODE:80045)
//				throw AMPException.getException(80045);
//			}
//		}
//
//		if (SMSTypeEnum.smstypeEnum.forgetpaypwd.toString().equals(smsType) ||
//				SMSTypeEnum.smstypeEnum.edittradepwd.toString().equals(smsType) ||
//				SMSTypeEnum.smstypeEnum.normal.toString().equals(smsType)) {
//			if (null == account) {
//				// error.define[80021]=用户账号不存在(CODE:80021)
//				throw AMPException.getException(80021);
//			}
//		}
//
//		if (SMSTypeEnum.smstypeEnum.login.toString().equals(smsType) ||
//				SMSTypeEnum.smstypeEnum.forgetlogin.toString().equals(smsType)) {
//			this.investorBaseAccountService.check2SysUserByPhone(phone, false);
//		}
    }

}
