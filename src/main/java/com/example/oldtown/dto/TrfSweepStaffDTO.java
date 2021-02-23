package com.example.oldtown.dto;

import com.example.oldtown.modules.trf.model.TrfSweepStaff;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * @author ding.yp
 * @name
 * @info
 * @date 2020/12/04
 */


public class TrfSweepStaffDTO {
    @JsonUnwrapped
    private TrfSweepStaff trfSweepStaff;
    private String sweepName;
    private String sweepSerial;
    private String sweepType;

    public TrfSweepStaffDTO(TrfSweepStaff trfSweepStaff,String sweepName, String sweepSerial, String sweepType) {
        this.trfSweepStaff = trfSweepStaff;
        this.sweepName = sweepName;
        this.sweepSerial = sweepSerial;
        this.sweepType = sweepType;
    }

    public TrfSweepStaff getTrfSweepStaff() {
        return trfSweepStaff;
    }

    public void setTrfSweepStaff(TrfSweepStaff trfSweepStaff) {
        this.trfSweepStaff = trfSweepStaff;
    }

    public String getSweepName() {
        return sweepName;
    }

    public void setSweepName(String sweepName) {
        this.sweepName = sweepName;
    }

    public String getSweepSerial() {
        return sweepSerial;
    }

    public void setSweepSerial(String sweepSerial) {
        this.sweepSerial = sweepSerial;
    }

    public String getSweepType() {
        return sweepType;
    }

    public void setSweepType(String sweepType) {
        this.sweepType = sweepType;
    }
}
