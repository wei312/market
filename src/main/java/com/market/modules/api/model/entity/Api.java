package com.market.modules.api.model.entity;

import com.market.base.framework.model.entity.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * api
 *
 * @author
 */
@ApiModel(value = "com.market.modules.api.model.entity.ApiAPI信息表")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Api extends BaseModel {

    /**
     * 请求URL
     */
    @ApiModelProperty(value = "请求URL")
    private String url;

    /**
     * 请求URL
     */
    @ApiModelProperty(value = "源URL")
    private String sourceUrl;
    
    /**
     * API名称
     */
    @ApiModelProperty(value = "API名称")
    private String name;

    /**
     * 请求方法
     */
    @ApiModelProperty(value = "请求方法")
    private String method;

    /**
     * 启用(1)-停用(0)状态
     */
    @ApiModelProperty("启用(1)-停用(0)状态")
    private String status;

    /**
     * API服务名称
     */
    @ApiModelProperty(value = "API服务名称")
    private String apiServiceName;

    /**
     * API服务名称英文
     */
    @ApiModelProperty(value = "API服务名称英文")
    private String apiService;

    /**
     * API服务分类名称
     */
    @ApiModelProperty(value = "API服务分类名称")
    private String apiCatalogueName;

    /**
     * API服务分类英文
     */
    @ApiModelProperty(value = "API服务分类英文")
    private String apiCatalogue;

    /**
     * 请求PATH
     */
    @ApiModelProperty(value = "请求PATH")
    private String path;

    /**
     * 请求HOST
     */
    @ApiModelProperty(value = "请求HOST")
    private String host;

    /**
     * 请求时CODE
     */
    @ApiModelProperty(value = "请求时CODE")
    private String runCode;
}