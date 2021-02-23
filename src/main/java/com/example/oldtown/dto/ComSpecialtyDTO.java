package com.example.oldtown.dto;

import com.example.oldtown.modules.com.model.ComSpecialty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.io.Serializable;
import java.util.List;

/**
 * @author ding.yp
 * @name
 * @info
 * @date 2020/12/24
 */


public class ComSpecialtyDTO implements Serializable {
    @JsonUnwrapped
    private ComSpecialty comSpecialty;
    private List<MinPlaceDTO> placeList;

    public ComSpecialtyDTO(ComSpecialty comSpecialty, List<MinPlaceDTO> placeList) {
        this.comSpecialty = comSpecialty;
        this.placeList = placeList;
    }

    public ComSpecialty getComSpecialty() {
        return comSpecialty;
    }

    public void setComSpecialty(ComSpecialty comSpecialty) {
        this.comSpecialty = comSpecialty;
    }

    public List<MinPlaceDTO> getPlaceList() {
        return placeList;
    }

    public void setPlaceList(List<MinPlaceDTO> placeList) {
        this.placeList = placeList;
    }
}
