package com.market.modules.api.controller;

import com.alibaba.fastjson.JSONArray;
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
import com.market.modules.api.model.dto.ApiDTO;
import com.market.modules.api.model.entity.Api;
import com.market.modules.api.model.param.ApiInsertParam;
import com.market.modules.api.model.param.ApiUpdateStatusParam;
import com.market.modules.api.service.ApiMarketService;
import com.market.modules.apicode.model.entity.ApiCode;
import com.market.modules.apicode.service.impl.ApiCodeService;
import com.market.modules.apiparam.model.entity.ApiParam;
import com.market.modules.apiparam.service.ApiParamService;
import com.market.modules.user.model.entity.UserPermissions;
import com.market.modules.user.service.UserPermissionsService;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * API控制器
 *
 * @Author wei
 * @Date 2021/2/22 16:37
 **/
@io.swagger.annotations.Api(tags = "Apis", description = "API市场")
@RestController
@RequestMapping("/api")
@Slf4j
public class ApiMarketController extends SuperController {

    @Autowired
    ApiMarketService apiMarketService;

    @Autowired
    UserPermissionsService userPermissionsService;

    @Autowired
    ApiCodeService apiCodeService;

    @Autowired
    ApiParamService apiParamService;

    @ApiOperation("API请求市场返回结果(调用外部接口)")
    @RequestMapping(value = "/{value}")
    public String getApi(@PathVariable("value") String value) {
        String userName = Tools.getParameter("userName");
        if (StringUtils.isEmpty(userName)) {
            return ModelAndView.error(410, "用户不能为空.").toJson();
        }
        log.info("当前请求参数 ,{}", value);
        if (StringUtils.isEmpty(value)) {
            return ModelAndView.error(value).toJson();
        }
        // 格式化参数
        String paramValue = (String) Tools.parseParam(value);
        // 按ID查询API配置
        Api api = apiMarketService.getOne(new QueryWrapper<Api>().lambda().eq(Api::getId, paramValue)
                .or(m -> m.eq(Api::getApiService, paramValue)));
        // API配置为空则抛异常,不为空时,则校验API状态,状态为1是启用,继续往下执行,是0为禁用,返回给前端
        if (api == null) {
            return ModelAndView.success(200, "API配置为空.").toJson();
        } else {
            if ("0".equalsIgnoreCase(api.getStatus())) {
                return ModelAndView.error(200, "API暂停使用中.").toJson();
            }
        }
        // API不为空,则查询用户权限为1的,有权限查询APICODE进行访问,无权限返回错误信息
        UserPermissions userPermissions = userPermissionsService
                .getOne(new QueryWrapper<UserPermissions>().lambda().eq(UserPermissions::getApiId, api.getId())
                        .and(m -> m.eq(UserPermissions::getUsername, userName))
                        .and(m -> m.eq(UserPermissions::getStatus, "1")));
        if (userPermissions == null) {
            return ModelAndView.success(200, "您无权访问此API.").toJson();
        } else {
            // 查询有效的CODE
            ApiCode apiCode = apiCodeService
                    .getOne(new QueryWrapper<ApiCode>().lambda().eq(ApiCode::getApiId, api.getId())
                            .and(m -> m.eq(ApiCode::getStatus, "1")));
            if (apiCode == null) {
                return ModelAndView.success(200, "API请求次数已用完,请重新获取").toJson();
            } else {
                // 设置到API调用时RUNCODE
                api.setRunCode(apiCode.getApiCode());
                // 获取请求参数集合
                List<ApiParam> apiParamList = apiParamService
                        .list(new QueryWrapper<ApiParam>().lambda().eq(ApiParam::getApiId, api.getId()));
                log.info(" apiParamList ,{} ", apiParamList);
                // 校验是否必输,是否有传值
                String parseValue = Tools.parseParameters(apiParamList);
                log.info(" parseValue ,{}", parseValue);
                // 校验结果不为空,则返回校验结果
                if (StringUtils.isNotEmpty(parseValue)) {
                    return parseValue;
                }
                // 组装请求参数,封装到MAP中
                Map<String, String> queryMap = Tools.getParameters(apiParamList);
                // 调用ALI市场接口,返回结果进行解析,解析后封装
                String responseString = Tools.sendHttpConnection(api, queryMap);
                JSONArray jsonArray = Tools.parseJson(responseString);
                return ModelAndView.successData(jsonArray).toJson();
            }
        }
    }

    @ApiOperation("API服务列表含请求参数等")
    @GetMapping("listAll")
    public String apiList(@RequestParam String userName, String searchKey) throws Exception {
        Page page = this.getPage();
        IPage apiIPage = null;
        // 关键字为空,则查询全部,关键字不为空,则精确匹配ApiCatalogueName字段或者模糊查询ApiServiceName字段
        LambdaQueryWrapper<Api> apiQueryWrapper = new QueryWrapper<Api>().lambda();
        if (StringUtils.isNotEmpty(searchKey)) {
            apiQueryWrapper.eq(StringUtils.isNotEmpty(searchKey), Api::getApiCatalogueName, searchKey).or(l -> l
                    .like(StringUtils.isNotEmpty(searchKey), Api::getApiServiceName, searchKey));
        }

        apiIPage = apiMarketService.selectPage(page, apiQueryWrapper);
        log.info("API服务列表 -> API IPage -{}", apiIPage.getTotal());
        if (apiIPage != null && apiIPage.getRecords().size() > 0) {
            List<Api> apiList = apiIPage.getRecords();
            List<ApiDTO> apiDTOList = new ArrayList<>();
            // 查询API请求参数,用户权限,拼成LIST,封装到ApiDTO实体中
            apiList.stream().forEach(api -> {
                ApiDTO apiDTO = new ApiDTO();
                BeanUtils.copyProperties(api, apiDTO);
                // 查询API请求参数
                List<ApiParam> apiParamList = apiParamService
                        .list(new QueryWrapper<ApiParam>().lambda().eq(ApiParam::getApiId, api.getId()));
                apiDTO.setApiParamList(apiParamList);
                // 查询用户权限()
                UserPermissions userPermissions = userPermissionsService
                        .getOne(new QueryWrapper<UserPermissions>().lambda()
                                .eq(UserPermissions::getApiId, api.getId())
                                .and(e -> e.eq(UserPermissions::getUsername, userName)));
                apiDTO.setUserStatus(userPermissions != null ? userPermissions.getStatus() : "-1");
                apiDTOList.add(apiDTO);
            });

            // 循环设置API的URL地址,用于前端测试或展示
            apiDTOList.stream().forEach(apiDTO -> {
                try {
                    apiDTO.setUrl(Tools.assemblyParameterUrl(apiDTO.getApiParamList(), apiDTO.getApiService()));
                } catch (Exception e) {
                    apiDTO.setUrl("");
                    e.printStackTrace();
                    log.error("设置URL失败 {}", e.getMessage());
                }
            });
            apiIPage.setRecords(apiDTOList);
            ModelAndView modelAndView = ModelAndView.ipage(apiIPage);
            return ModelAndView.toJSONString(modelAndView);
        } else {
            return ModelAndView.success(200, "").toJson();
        }
    }

    @ApiOperation("API新增")
    @PostMapping("insert")
    @Transactional
    public String insertApi(@RequestBody ApiInsertParam apiInsertParam) {
        // 按API行业分类与服务名校验是否存在
        Api api = apiMarketService
                .getOne(new QueryWrapper<Api>().lambda().eq(Api::getApiCatalogue, apiInsertParam.getApiCatalogue())
                        .and(e -> e.eq(Api::getApiService, apiInsertParam.getApiServiceName())));
        if (api != null) {
            return ModelAndView.error(414, "API行业分类与服务名重复").toJson();
        }
        api = new Api();
        BeanUtils.copyProperties(apiInsertParam, api);
        api.setId(Tools.getUUID());
        boolean b = apiMarketService.save(api);
        // 新增API时 ,有个API CODE 需要插入到另一张表,与 API表进行关联(关联字段api_id)
        // 请求此API接口时, 会将状态为1的APICODE 设置到 API表中的 RUNCODE字段,做为请求时APICODE
        if (StringUtils.isNotEmpty(apiInsertParam.getCode())) {
            ApiCode apiCode = new ApiCode();
            apiCode.setApiCode(apiInsertParam.getCode());
            apiCode.setApiId(api.getId());
            apiCode.setStatus("1");
            boolean apiCodeB = apiCodeService.save(apiCode);
        }
        return ModelAndView.success(200, "新增成功").toJson();
    }

    @ApiOperation("授权界面删除API,同时删除用户权限表,APICODE码表,请求参数表.")
    @PostMapping("delete")
    @Transactional
    public String deleteApi(@RequestBody String apiId) {
        // 删除API表
        log.info("APIID :{}", apiId);
        boolean apiB = apiMarketService.remove(new QueryWrapper<Api>().lambda().eq(Api::getId, apiId));
        if (apiB) {
            // 删除API配置表
            boolean apiParamB = apiParamService
                    .remove(new QueryWrapper<ApiParam>().lambda().eq(ApiParam::getApiId, apiId));
            // 删除API CODE配置表
            boolean apiCodeB = apiCodeService.remove(new QueryWrapper<ApiCode>().lambda().eq(ApiCode::getApiId, apiId));
            // 删除用户权限表
            boolean userPB = userPermissionsService
                    .remove(new QueryWrapper<UserPermissions>().lambda().eq(UserPermissions::getApiId, apiId));
            log.info("删除API {},删除API配置 {},删除APICODE配置 {},删除用户权限表 {} ", apiB, apiParamB, apiCodeB, userPB);
            if (apiParamB && apiCodeB && userPB) {
                return ModelAndView.success(200, "删除成功").toJson();
            } else {
                return ModelAndView.error(414, "删除失败.").toJson();
            }
        }
        return ModelAndView.success(200, "删除成功.").toJson();
    }

    @ApiOperation("操作APi状态")
    @PostMapping("updatestatus")
    public String updateApiStatus(@RequestBody ApiUpdateStatusParam apiUpdateStatusParam) {
        if (CollectionUtils.isEmpty(apiUpdateStatusParam.getIds())) {
            return ModelAndView.error("IDS不能为空.").toJson();
        }
        if (!("0".equalsIgnoreCase(
                apiUpdateStatusParam.getStatus()) || "1".equalsIgnoreCase(apiUpdateStatusParam.getStatus()))) {
            return ModelAndView.error("状态码必须为0和1.").toJson();
        }
        // 更新API状态
        boolean b = apiMarketService
                .update(new UpdateWrapper<Api>().lambda().set(Api::getStatus, apiUpdateStatusParam.getStatus())
                        .in(CollectionUtils.isNotEmpty(apiUpdateStatusParam.getIds()),
                                Api::getId, apiUpdateStatusParam.getIds()));
        if (b) {
            return ModelAndView.success(200, "成功.").toJson();
        } else {
            return ModelAndView.success(200, "失败.").toJson();
        }
    }
}
