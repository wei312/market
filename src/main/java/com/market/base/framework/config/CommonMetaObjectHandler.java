package com.market.base.framework.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.market.base.framework.utils.Tools;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.MetaObject;

/**
 * 通用填充类 适用于mybatis plus
 *
 * @author Caratacus
 */
public class CommonMetaObjectHandler implements MetaObjectHandler {

    /**
     * 表主键
     */
    private static final String id = "id";
    /**
     * 创建时间
     */
    private static final String createTime = "createTime";
    /**
     * 修改时间
     */
    private static final String updateTime = "updateTime";


    /**
     * INSERT时自动插入字段
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject.hasSetter(id) && metaObject.hasGetter(id)
                && metaObject.getSetterType(id).getName().equals("java.lang.String")
                && StringUtils.isBlank((String) metaObject.getValue(id))) {
            metaObject.setValue(id, Tools.getUUID());
        }
        setInsertFieldValByName(createTime, Tools.now(), metaObject);
        setUpdateFieldValByName(updateTime, Tools.now(), metaObject);
    }

    /**
     * 更新时自动填充字段
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        setUpdateFieldValByName(updateTime, Tools.now(), metaObject);
    }
}
