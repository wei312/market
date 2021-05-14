package com.market.base.framework.utils;

import org.apache.commons.lang.StringUtils;


/**
 * 直接操作数据库表的数据的CRUD工具类
 *
 * @author Thinkpad
 */

public class RdbUtil {

    /**
     * 格式化sql条件，主要把是'引号替换为''，防止sql注入
     * mongodb中把双引号替换为\"防止过滤条件中输入filters条件
     *
     * @param vStr
     * @return
     */
    public static String formatSqlValue(String vStr) {
        if (StringUtils.isBlank(vStr)) {
            return "";
        }
        return vStr.replace("'", "''").replace("\"", "\\\"");
    }

}
