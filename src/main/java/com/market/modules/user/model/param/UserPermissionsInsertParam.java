package com.market.modules.user.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户审核参数
 *
 * @Author wei
 * @Date 2021/2/23 11:32
 **/
@Data
public class UserPermissionsInsertParam {

    @ApiModelProperty("API的ID")
    private String apiId;

    @ApiModelProperty("用户ID")
    private String userName;

    @ApiModelProperty("审核备注")
    private String remark;
}
