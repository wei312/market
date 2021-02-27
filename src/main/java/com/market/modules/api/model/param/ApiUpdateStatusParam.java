package com.market.modules.api.model.param;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author wei
 * @Date 2021/2/26 15:01
 **/
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ApiUpdateStatusParam {

    @ApiModelProperty("APi的IDS")
    private List<String> ids;

    @ApiModelProperty("状态 ")
    private String status;
}
