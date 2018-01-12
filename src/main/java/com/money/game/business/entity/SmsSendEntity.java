package com.money.game.business.entity;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author conan
 *         2018/1/12 10:57
 **/
@Entity
@Table(name = "T_SMS_SEND")
@lombok.Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class SmsSendEntity implements Serializable {

    private static final long serialVersionUID = -9113750421676010255L;
    /**
     * 短信发送类型
     * 01:注册，02:快速登录，03:忘记登录密码，04:重置交易密码，05:活动
     */
    private String smsSendTypes;

    /**
     * 通知内容
     */
    private String notifyContent;

    /**
     * 通知状态	01:成功,02:失败
     */
    private String notifyStatus;

    /**
     * 错误代码
     */
    private String errorCode;

    /**
     * 错误消息
     */
    private String errorMessage;

    private Timestamp createTime;

    private Timestamp updateTime;
}
