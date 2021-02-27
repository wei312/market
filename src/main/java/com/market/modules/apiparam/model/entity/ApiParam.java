package com.market.modules.apiparam.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.market.base.framework.model.entity.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * api_param
 *
 * @author
 */
@ApiModel(value = "com.market.modules.apiparam.model.entity.ApiParam请求参数表")
@TableName("api_param")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ApiParam extends BaseModel {

    /**
     * API的ID
     */
    @ApiModelProperty(value = "API的ID")
    private String apiId;

    /**
     * 参数KEY
     */
    @ApiModelProperty(value = "参数KEY")
    private String keyName;

    /**
     * 类型
     */
    @ApiModelProperty(value = "类型")
    private String type;

    /**
     * 是否必须
     */
    @ApiModelProperty(value = "是否必须")
    private String isMust;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String remark;

    /**
     * 示例值
     */
    @ApiModelProperty("示例值")
    private String exampleValue;

}