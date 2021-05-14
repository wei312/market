package com.market;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = {"com.market.modules.*.mapper"})
@SpringBootApplication
public class MarketApplication {

    public static void main(String[] args) {

        SpringApplication.run(MarketApplication.class, args);
    }


}
