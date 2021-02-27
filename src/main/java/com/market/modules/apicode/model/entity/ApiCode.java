package com.market.modules.apicode.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * api_code
 *
 * @author
 */
@ApiModel(value = "com.market.modules.apicode.model.entity.ApiCodeAPI申请的 CODE表")
@TableName("api_code")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ApiCode implements Serializable {

    /**
     * 唯一ID
     */
    @ApiModelProperty(value = "唯一ID")
    private String id;

    /**
     * APi的ID
     */
    @ApiModelProperty(value = "APi的ID")
    private String apiId;

    /**
     * APi的CODE
     */
    @ApiModelProperty(value = "APi的CODE")
    private String apiCode;

    /**
     * 状态 1-启用,0-未启用,-1-已失效
     */
    @ApiModelProperty(value = "状态 1-启用,0-未启用,-1-已失效")
    private String status;

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