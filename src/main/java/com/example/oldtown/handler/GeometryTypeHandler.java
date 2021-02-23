package com.example.oldtown.handler;

import cn.hutool.extra.spring.SpringUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author ding.yp
 * @name mysql的geometry类型映射
 * @info
 * @date 2020/11/11
 */
@SuppressWarnings("rawtypes")
@MappedJdbcTypes(value = JdbcType.OTHER)
@MappedTypes(value = {String.class})
public class GeometryTypeHandler extends BaseTypeHandler<String> {
    private final Logger LOGGER = LoggerFactory.getLogger(GeometryTypeHandler.class);

    // @Override
    // public void setNonNullParameter(PreparedStatement preparedStatement, int i, Geometry geometry, JdbcType jdbcType) throws SQLException {
    //
    //     try {
    //         preparedStatement.setBytes(i, convertToBytes(geometry));
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, String s, JdbcType jdbcType) throws SQLException {
        try {
            preparedStatement.setBytes(i, convertFromWkt(s));
        } catch (IOException e) {
            LOGGER.warn("", e.getMessage());
        }
    }

    @Override
    public String getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return convertToWkt(resultSet.getBytes(s));

    }

    @Override
    public String getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return null; //convertToGeometry(resultSet.getBytes(i));
    }

    @Override
    public String getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return null; //convertToGeometry(callableStatement.getBytes(i));
    }

    /**
     * bytes转geometry对象
     * @param bytes
     * @return
     */
    private Geometry convertToGeometry(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            byte[] geomBytes = ByteBuffer.allocate(bytes.length - 4).order(ByteOrder.LITTLE_ENDIAN)
                    .put(bytes, 4, bytes.length - 4).array();
            Geometry geometry = SpringUtil.getBean(WKBReader.class).read(geomBytes);
            return (Point)geometry;
        } catch (Exception e) {
            LOGGER.warn("", e.getMessage());
        }
        return null;
    }

    /**
     * bytes转wkt对象
     * @param bytes
     * @return
     */
    private String convertToWkt(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            byte[] geomBytes = ByteBuffer.allocate(bytes.length - 4).order(ByteOrder.LITTLE_ENDIAN)
                    .put(bytes, 4, bytes.length - 4).array();

            Geometry geometry = SpringUtil.getBean(WKBReader.class).read(geomBytes);

            return geometry.toText();
        } catch (Exception e) {
            LOGGER.warn("", e.getMessage());
        }
        return null;
    }

    /**
     * geometry转bytes
     * @param geometry
     * @return
     * @throws IOException
     */
    private byte[] convertToBytes(Geometry geometry) throws IOException {
        byte[] geometryBytes = SpringUtil.getBean(WKBWriter.class).write(geometry);
        byte[] wkb = new byte[geometryBytes.length + 4];
        //设置SRID为4326
        ByteOrderValues.putInt(4326, wkb, ByteOrderValues.LITTLE_ENDIAN);
        System.arraycopy(geometryBytes, 0, wkb, 4, geometryBytes.length);
        return wkb;
    }

    /**
     * wkt转bytes
     * @return
     * @throws IOException
     */
    private byte[] convertFromWkt(String string) throws IOException {
        try {

            Geometry geometry = SpringUtil.getBean(WKTReader.class).read(string);
            byte[] geometryBytes = SpringUtil.getBean(WKBWriter.class).write(geometry);
            byte[] wkb = new byte[geometryBytes.length + 4];
            //设置SRID为4326
            ByteOrderValues.putInt(4326, wkb, ByteOrderValues.LITTLE_ENDIAN);
            System.arraycopy(geometryBytes, 0, wkb, 4, geometryBytes.length);
            return wkb;
        } catch (Exception e) {
            LOGGER.warn("", e.getMessage());
            return null;
        }
    }
}
