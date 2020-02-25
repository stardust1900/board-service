package com.allinpay.data.board.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import javax.validation.constraints.Size;

/**
 * @author : JoeTao
 * createAt: 2018/9/17
 */
@Builder
@Data
public class User {
    @ApiModelProperty(value = "用户名", required = true)
    @Size(min=1, max=20)
    private String name;
    @ApiModelProperty(value = "密码", required = true)
    @Size(min=6, max=20)
    private String password;
    
    @Tolerate
    User(){}
    
}
