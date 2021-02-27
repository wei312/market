package com.market.base.framework.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 父类 公共字段
 *
 * @Author wei
 * @Date 2021/2/27 14:27
 **/
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BaseModel {

    @TableId(type = IdType.UUID)
    @ApiModelProperty(notes = "数据库表唯一id(主健)")
    @TableField(fill = FieldFill.INSERT)
    private String id;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(notes = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(notes = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String updateTime;
}
