package com.money.game.business.entity;

import com.money.game.basic.component.ext.hibernate.UUID;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 普通用户
 * @author conan
 *         2018/1/4 16:12
 **/
@Entity
@Table(name = "T_USER")
@lombok.Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class UserEntity extends UUID implements Serializable {

    private static final long serialVersionUID = 6892987120963066904L;
    /**
     * 用户名
     */
    private String userAccount;

    /**登录密码 */
    private String userPwd;

    /**随机密钥 */
    private String salt;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 用户类型 01:普通用户,02:系统账号
     */
    private String userType;

    /**
     * 身份证号
     */
    private String idNum;

    /**
     * 邀请用户标识
     */
    private String uid;

    /**
     * 状态 01 正常, 02 冻结
     */
    private String status;

    /**支付密码 */
    private String payPwd;

    /**支付密码随机密钥 */
    private String paySalt;

    private Timestamp updateTime;

    private Timestamp createTime;
}
