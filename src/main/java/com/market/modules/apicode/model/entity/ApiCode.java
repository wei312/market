package com.market.modules.apicode.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.market.base.framework.model.entity.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@EqualsAndHashCode(callSuper = true)
public class ApiCode extends BaseModel {

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

}