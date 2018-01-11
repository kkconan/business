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
 * 后台系统用户
 * @author conan
 *         2018/1/4 16:12
 **/
@Entity
@Table(name = "T_SYS_USER")
@lombok.Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class SysUserEntity extends UUID implements Serializable{

    private static final long serialVersionUID = 1559643001683229066L;
    /**
     * 用户名
     */
    private String userName;

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
     * 身份证号
     */
    private String idNum;

    /**
     * 状态 normal 正常, forbidden 冻结
     */
    private String status;

    /**
     * 角色 admin 超级管理员,operation 运营人员
     */
    private String role;

    private Timestamp updateTime;

    private Timestamp createTime;
}
