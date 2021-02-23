package com.example.oldtown.dto;

import java.io.Serializable;
import java.util.HashMap;

public class ParkingStatisDTo implements Serializable {
    private  Integer totalNum;
    private  Double  duration;
    private HashMap<Integer,Integer> inNumByHours;
    private HashMap<Integer,Integer> outNumByHours;

    public ParkingStatisDTo(Integer totalNum, Double duration, HashMap<Integer,Integer> inNumByHours,HashMap<Integer,Integer> outNumByHours) {
        this.totalNum = totalNum;
        this.duration = duration;
        this.inNumByHours = inNumByHours;
        this.outNumByHours = outNumByHours;
    }


    public Integer getTotalNum() {
        return totalNum;
    }

    public Double getDuration() {
        return duration;
    }

    public HashMap<Integer, Integer> getInNumByHours() {
        return inNumByHours;
    }

    public HashMap<Integer, Integer> getOutNumByHours() {
        return outNumByHours;
    }
}
