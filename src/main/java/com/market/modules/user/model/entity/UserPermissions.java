package com.market.modules.user.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 用户权限表
 *
 * @author
 */
@ApiModel(value = "com.market.modules.user.model.entity.UserPermissions用户权限表")
@TableName("user_permissions")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserPermissions implements Serializable {

    /**
     * 唯一ID
     */
    @TableId
    @ApiModelProperty(value = "唯一ID")
    private String id;

    /**
     * API的ID
     */
    @ApiModelProperty(value = "API的ID")
    private String apiId;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private String username;


    /**
     * 状态:0待审核,1.审核通过,2,驳回
     */
    @ApiModelProperty("状态:0待审核,1.审核通过,2,驳回")
    private String status;

    /**
     * 审核描述
     */
    @ApiModelProperty("审核描述")
    private String remark;

    /**
     * 创建时间
     */
    @TableField
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    private static final long serialVersionUID = 1L;
}