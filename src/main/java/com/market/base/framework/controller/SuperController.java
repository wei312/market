package com.market.base.framework.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.market.base.framework.bean.ServerResponse;
import com.market.base.framework.constant.PageCons;
import com.market.base.framework.utils.Tools;
import com.market.base.framework.utils.TypeUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

/**
 * SuperController
 *
 * @author Caratacus
 */
public class SuperController {

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    /**
     * 成功返回
     *
     * @param object
     * @return
     */
    public <T> ServerResponse<T> success(T object) {
        return new ServerResponse<>(object);
    }

    /**
     * 成功返回
     *
     * @return
     */
    public ServerResponse<Void> success() {
        return new ServerResponse(HttpStatus.OK);
    }

    /**
     * 成功返回
     *
     * @param status
     * @param object
     * @return
     */
    public <T> ServerResponse<T> success(HttpStatus status, T object) {
        return new ServerResponse<T>(object);
    }


    /**
     * 成功返回
     *
     * @param status
     * @return
     */
    public ServerResponse<Void> success(HttpStatus status) {
        return new ServerResponse(status);
    }


    /**
     * 获取分页对象
     *
     * @return
     */
    protected <T> Page<T> getPage() {
        return getPage(true);
    }

    /**
     * 获取分页对象
     *
     * @param openSort
     * @return
     */
    protected <T> Page<T> getPage(boolean openSort) {
        int index = 1;
        // 页数
        Integer cursor = TypeUtils.castToInt(Tools.getParameter(PageCons.PAGE_PAGE), index);
        // 分页大小
        Integer limit = TypeUtils.castToInt(Tools.getParameter(PageCons.PAGE_ROWS), PageCons.DEFAULT_LIMIT);
        // 是否查询分页
        Boolean searchCount = TypeUtils.castToBoolean(Tools.getParameter(PageCons.SEARCH_COUNT), true);
        limit = limit > PageCons.MAX_LIMIT ? PageCons.MAX_LIMIT : limit;
        Page<T> page = new Page<>(cursor, limit, searchCount);
//        if (openSort) {
//            String[] p1 = getParameterSafeValues(PageCons.PAGE_ASCS);
//            if (Objects.nonNull(p1)) {
//                page.setAsc(p1);
//            }
//            String[] p2 = getParameterSafeValues(PageCons.PAGE_DESCS);
//            if (Objects.nonNull(p2)) {
//                page.setAsc(p2);
//            }
//        }
        return page;
    }

}
