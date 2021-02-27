package com.market.modules.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.market.base.framework.controller.SuperController;
import com.market.base.framework.utils.ModelAndView;
import com.market.base.framework.utils.Tools;
import com.market.modules.api.model.dto.ApiUserPerDTO;
import com.market.modules.api.model.entity.Api;
import com.market.modules.api.service.ApiMarketService;
import com.market.modules.apiparam.model.entity.ApiParam;
import com.market.modules.apiparam.service.ApiParamService;
import com.market.modules.user.model.entity.UserPermissions;
import com.market.modules.user.model.param.UserPermissionsInsertParam;
import com.market.modules.user.model.param.UserPermissionsParam;
import com.market.modules.user.service.UserPermissionsService;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author wei
 * @Date 2021/2/23 11:26
 **/
@RestController
@RequestMapping("/user/permissions")
@io.swagger.annotations.Api(tags = "UserPermissions", description = "用户权限信息接口")
@Validated
@Slf4j
public class UserPermissionsController extends SuperController {

    @Autowired
    UserPermissionsService userPermissionsService;

    @Autowired
    ApiMarketService apiMarketService;

    @Autowired
    ApiParamService apiParamService;

    @ApiOperation("用户权限列表(包含待审核,已审核,驳回)")
    @GetMapping("list")
    public String getUserPermissionsList(@RequestParam String userName, String searchKey) {
        Page page = this.getPage();
        IPage apiIPage = null;
        List<UserPermissions> userPermissionsList = userPermissionsService
                .list(new QueryWrapper<UserPermissions>().lambda()
                        .eq(UserPermissions::getUsername, userName)
                        .groupBy(UserPermissions::getApiId
                        ));
        // 若用户权限列表不为空则进行以下查询,否则返回空DATA
        if (CollectionUtils.isNotEmpty(userPermissionsList)) {
            // 去重后得到API的LIST
            List<String> apiIds = userPermissionsList.stream().map(m -> m.getApiId()).collect(Collectors.toList());
            // 按APIID查询所有的API列表
            LambdaQueryWrapper<Api> apiQueryWrapper = new QueryWrapper<Api>().lambda().in(Api::getId, apiIds);
            if (StringUtils.isNotEmpty(searchKey)) {
                apiQueryWrapper.and(StringUtils.isNotEmpty(searchKey),
                        l -> l.like(Api::getApiServiceName, searchKey).or(e -> e.eq(
                                Api::getApiCatalogueName, searchKey)));
            }
            apiQueryWrapper.orderByDesc(true, Api::getCreateTime);
            apiIPage = apiMarketService
                    .selectPage(page, apiQueryWrapper);
            List<Api> apiList = apiIPage.getRecords();
            List<ApiUserPerDTO> apiUserPerDTOList = new ArrayList<>();
            // 再按API列表中的信息查询用户权限列表,API请求参数,并拼接到ApiUserPerDTO中
            apiList.stream().forEach(api -> {
                ApiUserPerDTO apiUserPerDTO = new ApiUserPerDTO();
                BeanUtils.copyProperties(api, apiUserPerDTO);
                // 查询用户权限列表
                List<UserPermissions> userPermissionsNewList = userPermissionsService
                        .list(new QueryWrapper<UserPermissions>().lambda().eq(UserPermissions::getApiId, api.getId())
                                .and(e -> e.eq(UserPermissions::getUsername, userName)));
                apiUserPerDTO.setUserPermissionsList(userPermissionsNewList);
                // 查询API请求参数
                List<ApiParam> apiParamList = apiParamService
                        .list(new QueryWrapper<ApiParam>().lambda().eq(ApiParam::getApiId, api.getId()));
                apiUserPerDTO.setApiParamList(apiParamList);
                apiUserPerDTOList.add(apiUserPerDTO);
            });
            // 循环设置API的URL地址,用于前端测试或展示
            apiUserPerDTOList.stream().forEach(apiUserPerDTO -> {
                try {
                    apiUserPerDTO.setUrl(Tools
                            .assemblyParameterUrl(apiUserPerDTO.getApiParamList(), apiUserPerDTO.getApiService()));
                } catch (Exception e) {
                    apiUserPerDTO.setUrl("");
                    e.printStackTrace();
                    log.error("设置URL失败 {}", e.getMessage());
                }
            });
            // 返回MODEL JSON
            apiIPage.setRecords(apiUserPerDTOList);
            return ModelAndView.toJSONString(ModelAndView.ipage(apiIPage));
        } else {
            return ModelAndView.success(200, "").toJson();
        }
    }

    @ApiOperation("新增权限接口")
    @PostMapping("insert")
    public String insertUserPermissions(@RequestBody UserPermissionsInsertParam userPermissionsInsertParam) {
        // 校验参数
        if (StringUtils.isEmpty(userPermissionsInsertParam.getApiId())) {
            return ModelAndView.error("ID不能为空.").toJson();
        }
        if (StringUtils.isEmpty(userPermissionsInsertParam.getUserName())) {
            return ModelAndView.error("userName不能为空.").toJson();
        }
        // 校验API是否存在
        Api api = apiMarketService
                .getOne(new QueryWrapper<Api>().lambda().eq(Api::getId, userPermissionsInsertParam.getApiId()));
        if (api == null) {
            return ModelAndView.success(200, "API配置不存在.").toJson();
        }
        // 校验权限(目前是无论是否存在,都返回200及成功的状态)
        UserPermissions userPermissions = userPermissionsService.getOne(new QueryWrapper<UserPermissions>().lambda()
                .eq(UserPermissions::getApiId, userPermissionsInsertParam.getApiId())
                .and(e -> e.eq(UserPermissions::getUsername, userPermissionsInsertParam.getUserName())));
        if (userPermissions != null) {
            return ModelAndView.success(200, "成功.").toJson();
        }
        // 插入数据
        userPermissions = new UserPermissions();
        userPermissions.setApiId(userPermissionsInsertParam.getApiId());
        userPermissions.setUsername(userPermissionsInsertParam.getUserName());
        userPermissions.setRemark(userPermissionsInsertParam.getRemark());
        userPermissions.setStatus("1");
        boolean b = userPermissionsService.save(userPermissions);
        return ModelAndView.successData(b).toJson();
    }

    @ApiOperation("权限审核接口")
    @PostMapping("check")
    public String updateUserPermissionsList(@RequestBody UserPermissionsParam userPermissionsParam) {
        if (CollectionUtils.isEmpty(userPermissionsParam.getIds())) {
            return ModelAndView.error("IDS不能为空.").toJson();
        }
        if (!("1".equalsIgnoreCase(
                userPermissionsParam.getStatus()) || "2".equalsIgnoreCase(userPermissionsParam.getStatus()))) {
            return ModelAndView.error("状态码必须为1和2.").toJson();
        }
        // 设置审核状态,审核信息,按IDS查询设置
        boolean b = userPermissionsService.update(new UpdateWrapper<UserPermissions>().lambda()
                .set(UserPermissions::getStatus, userPermissionsParam.getStatus())
                .set(UserPermissions::getRemark, userPermissionsParam.getRemark())
                .set(UserPermissions::getUpdateTime, Tools.now()).in(
                        CollectionUtils.isNotEmpty(userPermissionsParam.getIds()), UserPermissions::getApiId,
                        userPermissionsParam.getIds()));
        return ModelAndView.successData(b).toJson();
    }

    @ApiOperation("待审核用户权限列表(管理员可见)")
    @GetMapping("check/list")
    public String checkUserPermissionsList(String searchKey) {
        Page page = this.getPage();
        // 有权限的超级管理员才能进行此接口
        IPage apiIPage = null;
        // 查询待审核的用户权限列表.按APIID去重得到API的所有ID
        List<UserPermissions> userPermissionsList = userPermissionsService
                .list(new QueryWrapper<UserPermissions>().lambda().eq(UserPermissions::getStatus, "0")
                        .groupBy(UserPermissions::getApiId
                        ));
        // 若用户权限列表不为空则进行以下查询,否则返回空DATA
        if (CollectionUtils.isNotEmpty(userPermissionsList)) {
            // 去重后得到API的LIST
            List<String> apiIds = userPermissionsList.stream().map(m -> m.getApiId()).collect(Collectors.toList());
            // 按APIID查询所有的API列表
            LambdaQueryWrapper<Api> apiQueryWrapper = new QueryWrapper<Api>().lambda().in(Api::getId, apiIds);
            if (StringUtils.isNotEmpty(searchKey)) {
                apiQueryWrapper.and(StringUtils.isNotEmpty(searchKey),
                        l -> l.like(Api::getApiServiceName, searchKey).or(e -> e.eq(
                                Api::getApiCatalogueName, searchKey)));
            }
            apiQueryWrapper.orderByDesc(true, Api::getCreateTime);
            apiIPage = apiMarketService
                    .selectPage(page, apiQueryWrapper);
            List<Api> apiList = apiIPage.getRecords();
            List<ApiUserPerDTO> apiUserPerDTOList = new ArrayList<>();
            // 再按API列表中的信息查询用户权限列表,API请求参数,并拼接到ApiUserPerDTO中
            apiList.stream().forEach(api -> {
                ApiUserPerDTO apiUserPerDTO = new ApiUserPerDTO();
                BeanUtils.copyProperties(api, apiUserPerDTO);
                // 查询用户权限列表
                List<UserPermissions> userPermissionsNewList = userPermissionsService
                        .list(new QueryWrapper<UserPermissions>().lambda().eq(UserPermissions::getApiId, api.getId())
                                .and(e -> e.eq(UserPermissions::getStatus, "0")));
                apiUserPerDTO.setUserPermissionsList(userPermissionsNewList);
                // 查询API请求参数
                List<ApiParam> apiParamList = apiParamService
                        .list(new QueryWrapper<ApiParam>().lambda().eq(ApiParam::getApiId, api.getId()));
                apiUserPerDTO.setApiParamList(apiParamList);
                apiUserPerDTOList.add(apiUserPerDTO);
            });
            // 循环设置API的URL地址,用于前端测试或展示
            apiUserPerDTOList.stream().forEach(apiUserPerDTO -> {
                try {
                    apiUserPerDTO.setUrl(Tools
                            .assemblyParameterUrl(apiUserPerDTO.getApiParamList(), apiUserPerDTO.getApiService()));
                } catch (Exception e) {
                    apiUserPerDTO.setUrl("");
                    e.printStackTrace();
                    log.error("设置URL失败 {}", e.getMessage());
                }
            });
            // 回设结果集
            apiIPage.setRecords(apiUserPerDTOList);
            // 返回MODEL JSON
            return ModelAndView.toJSONString(ModelAndView.ipage(apiIPage));
        } else {
            return ModelAndView.success(200, "").toJson();
        }
    }
}
