package com.example.oldtown.dto;

import java.io.Serializable;
import java.util.List;

public class TicketSaleDto  implements Serializable {
    private String type;
    private List<String> dateList;
    private List<Double> thisYearSaleList;
    private List<Double> lastYearSaleList;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getDateList() {
        return dateList;
    }

    public void setDateList(List<String> dateList) {
        this.dateList = dateList;
    }

    public List<Double> getThisYearSaleList() {
        return thisYearSaleList;
    }

    public void setThisYearSaleList(List<Double> saleNumList) {
        thisYearSaleList = saleNumList;
    }

    public List<Double> getLastYearSaleList() {
        return lastYearSaleList;
    }

    public void setLastYearSaleList(List<Double> saleList) {
        lastYearSaleList = saleList;
    }
}
