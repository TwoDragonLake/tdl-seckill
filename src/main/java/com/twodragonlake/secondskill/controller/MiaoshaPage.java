package com.twodragonlake.secondskill.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import com.twodragonlake.secondskill.model.Goods;
import com.twodragonlake.secondskill.service.GoodsService;
import com.twodragonlake.twodragonlakemvc.framework.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/p/")
public class MiaoshaPage
{
	@Autowired
	private GoodsService goodsService;

	@RequestMapping(value = "list")
	public String list(Map<String, Object> returnMap)
	{
		List<Goods> list = goodsService.getGoodsList();
		returnMap.put("list", list);

		return "list";
	}

	@RequestMapping(value = "{goodsId}/detail")
	public String detail(Integer goodsId, Map<String, Object> returnMap)
	{
		Assert.notNull(goodsId);

		Goods goods = goodsService.getDetail(goodsId);

		Assert.notNull(goods);

		returnMap.put("goods", goods);

		return "detail";

	}
}
