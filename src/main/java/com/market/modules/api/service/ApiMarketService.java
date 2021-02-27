package com.market.modules.api.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.market.base.framework.service.BaseService;
import com.market.modules.api.model.entity.Api;

/**
 * @Author wei
 * @Date 2021/2/22 16:48
 **/
public interface ApiMarketService extends BaseService<Api> {

    IPage selectPage(Page page, Wrapper<Api> queryWrapper);

}
