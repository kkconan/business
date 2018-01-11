package com.money.game.business.dto.client;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @author conan
 *         2017/11/6 19:20
 **/
@Data
public class UserLoginDto implements Serializable {

    private static final long serialVersionUID = -5410960488032347272L;


    @NotBlank(message = "账号不能为空！")
    @ApiModelProperty(value = "用户账号")
    private String userAccount;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String userPwd;

    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码")
    private String vericode;

}
