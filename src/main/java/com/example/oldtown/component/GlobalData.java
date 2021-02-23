package com.example.oldtown.component;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * @author ding.yp
 * @name
 * @info
 * @date 2020/9/25
 */
@Component
public class GlobalData {
    public static Long ROLE_SUPER_ID = 1L;
    public static Long PERMISSION_SUPER_ID = 10L;
    public static String NMS_TOKEN = "";
    public static String JSAPI_TICKET = "";
    public static String OFFICIAL_ACCESS_TOKEN = "";
    public static String SYS_ADMIN = "sysAdmin";
    public static String XCX_USER = "xcxUser";
    public static String TRF_SECURITY_STAFF = "trfSecurityStaff";
    public static String COM_PLACE = "comPlace";
    public static String COM_POI = "comPoi";
    public static String COM_ROUTE = "comRoute";
    public static List<String> EXCEL_TYPES = Arrays.asList("application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    public static List<String> getExcelTypes() {
        return EXCEL_TYPES;
    }

    public static void setExcelTypes(List<String> excelTypes) {
        EXCEL_TYPES = excelTypes;
    }

    @PostConstruct
    public void init() {


    }

    public static Long getRoleSuperId() {
        return ROLE_SUPER_ID;
    }

    public static void setRoleSuperId(Long roleSuperId) {
        ROLE_SUPER_ID = roleSuperId;
    }

    public static String getNmsToken() {
        return NMS_TOKEN;
    }

    public static void setNmsToken(String nmsToken) {
        NMS_TOKEN = nmsToken;
    }

    public static String getJsapiTicket() {
        return JSAPI_TICKET;
    }

    public static void setJsapiTicket(String jsapi_ticket) {
        JSAPI_TICKET = jsapi_ticket;
    }

    public static String getOfficialAccessToken() {
        return OFFICIAL_ACCESS_TOKEN;
    }

    public static void setOfficialAccessToken(String officialAccessToken) {
        OFFICIAL_ACCESS_TOKEN = officialAccessToken;
    }
}
