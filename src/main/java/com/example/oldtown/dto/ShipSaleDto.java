package com.example.oldtown.dto;

import java.io.Serializable;
import java.util.List;

public class ShipSaleDto implements Serializable {
    private String Type;
    private List<String> DateList;
    private List<Integer> SaleNumList;
    private List<Double> SaleAmountList;

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public List<String> getDateList() {
        return DateList;
    }

    public void setDateList(List<String> dateList) {
        DateList = dateList;
    }

    public List<Integer> getSaleNumList() {
        return SaleNumList;
    }

    public void setSaleNumList(List<Integer> saleNumList) {
        SaleNumList = saleNumList;
    }

    public List<Double> getSaleAmountList() {
        return SaleAmountList;
    }

    public void setSaleAmountList(List<Double> saleAmountList) {
        SaleAmountList = saleAmountList;
    }
}
