package com.market.base.framework.config;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MybatisPlus 配置
 *
 * @author Caratacus
 */
@Configuration
public class MybatisPlusAutoConfiguration {

    /**
     * 分页
     *
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 乐观锁
     *
     * @return
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

    
    /**
     * 自动填充
     *
     * @return
     */
    @Bean
    public CommonMetaObjectHandler commonMetaObjectHandler() {
        return new CommonMetaObjectHandler();
    }


}
