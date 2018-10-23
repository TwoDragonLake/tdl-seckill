package com.tdl.seckill.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Description.
 *
 * @author : CeaserWang
 * @version : 1.0
 * @since : 2018/10/23 23:06
 */
@Data
public class SeckillGoodsBO  {
    private Long id;

    private Long sale;

    private Long total;

    private Long version;

    private Integer status;

    private Long userId;
}
