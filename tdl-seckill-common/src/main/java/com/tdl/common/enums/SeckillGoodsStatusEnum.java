package com.tdl.common.enums;

import java.io.InputStream;

/**
 * Description.
 *
 * @author : CeaserWang
 * @version : 1.0
 * @since : 2018/10/6 22:03
 */
public enum SeckillGoodsStatusEnum {

    enabled(1,"生效"),
    disabled(2,"失效");

     SeckillGoodsStatusEnum(Integer code,String name){
        this.code = code;
        this.name  = name;
    }

    private Integer code;
    private String name;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
