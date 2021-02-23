package com.example.oldtown.component;

import cn.hutool.extra.spring.SpringUtil;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.ByteOrderValues;
import org.locationtech.jts.io.WKBReader;
import org.locationtech.jts.io.WKBWriter;
import org.locationtech.jts.io.WKTReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

/**
 * @author ding.yp
 * @name 有关jts的bean注入
 * @info
 * @date 2020/11/11
 */

@Component
@ComponentScan(value = {"cn.hutool.extra.spring"})
@Import(SpringUtil.class)
public class JtsBean {

    private GeometryFactory geometryFactory;

    public JtsBean() {
        geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    }

    @Bean
    public WKBWriter wkbWriter() {
        return new WKBWriter(2, ByteOrderValues.LITTLE_ENDIAN);
    }

    @Bean
    public WKBReader wkbReader() {
        return new WKBReader(geometryFactory);
    }

    @Bean
    public WKTReader wktReader() {
        return new WKTReader(geometryFactory);
    }
}
