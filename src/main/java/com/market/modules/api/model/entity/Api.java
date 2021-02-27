package com.market.modules.api.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
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
@EqualsAndHashCode(callSuper = false)
public class Api implements Serializable {

    /**
     * 唯一ID
     */
    @ApiModelProperty(value = "唯一ID")
    private String id;

    /**
     * 请求URL
     */
    @ApiModelProperty(value = "请求URL")
    private String url;

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
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private String updateTime;

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