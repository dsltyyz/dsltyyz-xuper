package com.dsltyyz.xuper.xuperchain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(description = "账号VO")
@Data
public class AccountVO implements Serializable {

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "助记词")
    private String mnemonic;
}
