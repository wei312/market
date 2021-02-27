package com.market.modules.api.model.dto;

import com.market.modules.api.model.entity.Api;
import com.market.modules.apiparam.model.entity.ApiParam;
import com.market.modules.user.model.entity.UserPermissions;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author wei
 * @Date 2021/2/23 14:47
 **/
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ApiUserPerDTO extends Api {


    /**
     * APi请求参数
     */
    @ApiModelProperty("APi请求参数")
    List<ApiParam> apiParamList;
    /**
     * 用户权限集合
     */
    @ApiModelProperty("用户权限集合")
    List<UserPermissions> userPermissionsList;
}
