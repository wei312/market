package com.market.modules.apiparam.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
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
@EqualsAndHashCode(callSuper = false)
public class ApiParam implements Serializable {

    /**
     * 参数唯一ID
     */
    @ApiModelProperty(value = "参数唯一ID")
    private String id;

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
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    private static final long serialVersionUID = 1L;
}