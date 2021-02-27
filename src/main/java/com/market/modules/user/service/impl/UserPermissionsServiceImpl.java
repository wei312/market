package com.market.modules.user.service.impl;

import com.market.base.framework.service.impl.BaseServiceImpl;
import com.market.modules.user.mapper.UserPermissionsMapper;
import com.market.modules.user.model.entity.UserPermissions;
import com.market.modules.user.service.UserPermissionsService;
import org.springframework.stereotype.Service;

/**
 * @Author wei
 * @Date 2021/2/22 19:19
 **/
@Service
public class UserPermissionsServiceImpl extends BaseServiceImpl<UserPermissionsMapper, UserPermissions> implements
        UserPermissionsService {

}
