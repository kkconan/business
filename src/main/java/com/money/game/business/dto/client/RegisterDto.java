package com.money.game.business.dto.client;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @author conan
 *         2018/1/5 10:43
 **/
@Data
public class RegisterDto implements Serializable {

    private static final long serialVersionUID = 495802275557800297L;
    @ApiModelProperty(value = "用户账号")
    private String userAccount;

    @ApiModelProperty(value = "用户手机号")
    private String phone;

    @ApiModelProperty(value = "验证码")
    private String vericode;

    /**
     * 用户密码
     */
    @NotBlank(message = "登录密码不能为空！")
    @ApiModelProperty(value = "密码")
    private String userPwd;

    /**
     * 图形验证码
     */
    @ApiModelProperty(value = "图像验证码")
    private String imgvc;

    /**
     * 邀请人邀请码
     */
    @ApiModelProperty(value = "邀请人id")
    private String sceneId;


}
