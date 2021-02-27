package com.market.base.framework.model.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author wei
 * @Date 2021/2/24 10:11
 **/
@Data
public class PageObject {

    @ApiModelProperty("当前页")
    private int current;
    @ApiModelProperty("当前页数")
    private int size;
}
