package com.example.oldtown.dto;

/**
 * @author ding.yp
 * @name
 * @info
 * @date 2020/12/07
 */


public class IdNameDTO {
    private Long id;
    private String name;

    public IdNameDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
