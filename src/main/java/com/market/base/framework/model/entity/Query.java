/**
 * 2019 东方金信
 */

package com.market.base.framework.model.entity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.market.base.framework.constant.PageCons;
import java.util.Map;

/**
 * 查询参数
 *
 * @author Mark sunlightcs@gmail.com
 */
public class Query<T> {

    public IPage<T> getPage(Map<String, Object> params) {
        return this.getPage(params, null, false);
    }

    public IPage<T> getPage(Map<String, Object> params, String defaultOrderField, boolean isAsc) {
        //分页参数
        long curPage = 1;
        long limit = 10;

        if (params.get(PageCons.PAGE_PAGE) != null) {
            curPage = Long.parseLong((String) params.get(PageCons.PAGE_PAGE));
        }
        if (params.get(PageCons.PAGE_ROWS) != null) {
            limit = Long.parseLong((String) params.get(PageCons.PAGE_ROWS));
        }

        //分页对象
        Page<T> page = new Page<>(curPage, limit);
        new Page<>(curPage, limit, true);

        //分页参数
        params.put(PageCons.PAGE_PAGE, page);

        //排序字段
//        //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
//        String orderField = SQLFilter.sqlInject((String) params.get(Constant.ORDER_FIELD));
//        String order = (String) params.get(Constant.ORDER);
//
//        //前端字段排序
//        if (StringUtils.isNotEmpty(orderField) && StringUtils.isNotEmpty(order)) {
//            if (Constant.ASC.equalsIgnoreCase(order)) {
//                return page.setAsc(orderField);
//            } else {
//                return page.setDesc(orderField);
//            }
//        }

        //默认排序
        if (isAsc) {
            page.setAsc(defaultOrderField);
        } else {
            page.setDesc(defaultOrderField);
        }

        return page;
    }
}
