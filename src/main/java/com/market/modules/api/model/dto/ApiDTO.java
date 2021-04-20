package com.market.modules.api.model.dto;

import com.market.modules.api.model.entity.Api;
import com.market.modules.apiparam.model.entity.ApiParam;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author wei
 * @Date 2021/2/23 10:36
 **/
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ApiDTO extends Api {

    /**
     * API请求参数
     */
    @ApiModelProperty("API请求参数")
    List<ApiParam> apiParamList;

    /**
     * 用户申请/权限状态
     */
    @ApiModelProperty("用户申请/权限状态")
    private String userStatus;

    /**
     * 用户申请/权限状态
     */
    @ApiModelProperty("用户名称")
    private String userName;

}
