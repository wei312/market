package com.market.modules.user.model.dto;

import com.market.modules.api.model.entity.Api;
import com.market.modules.user.model.entity.UserPermissions;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 授权列表界面实体
 *
 * @Author wei
 * @Date 2021/2/23 11:56
 **/
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserPermissionsDTO extends UserPermissions {

    /**
     * 关联API列表
     */
    @ApiModelProperty(value = "关联API列表")
    private List<Api> apiList;
}
