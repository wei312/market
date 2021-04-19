package com.market.modules.api.model.param;

import com.market.modules.api.model.entity.Api;
import com.market.modules.apiparam.model.entity.ApiParam;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * API INSERT参数
 *
 * @Author wei
 * @Date 2021/2/25 15:28
 **/
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ApiInsertParam extends Api {

    /**
     * API CODE码
     */
    @ApiModelProperty("API CODE码")
    private String code;


    /**
     * 请求参数
     */
    @ApiModelProperty(value = "请求参数")
    private List<ApiParam> apiParams;

}
