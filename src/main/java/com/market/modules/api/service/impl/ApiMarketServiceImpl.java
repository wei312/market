package com.market.modules.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.market.base.framework.service.impl.BaseServiceImpl;
import com.market.modules.api.mapper.ApiMapper;
import com.market.modules.api.model.entity.Api;
import com.market.modules.api.service.ApiMarketService;
import org.springframework.stereotype.Service;

/**
 * @Author wei
 * @Date 2021/2/22 16:49
 **/
@Service
public class ApiMarketServiceImpl extends BaseServiceImpl<ApiMapper, Api> implements ApiMarketService {

    public IPage selectPage(Page page, Wrapper<Api> queryWrapper) {
        return baseMapper.selectPage(page, queryWrapper);
    }

}
