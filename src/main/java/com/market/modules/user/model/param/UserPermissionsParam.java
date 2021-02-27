package com.market.modules.user.model.param;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * 用户授权参数
 *
 * @Author wei
 * @Date 2021/2/23 11:32
 **/
@Data
public class UserPermissionsParam {

    @ApiModelProperty("授权表的IDES")
    private List<String> ids;

    @ApiModelProperty("授权状态")
    private String status;

    @ApiModelProperty("审核信息")
    private String remark;
}
