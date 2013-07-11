package com.crawl.dianping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.model.Comment;
import com.model.Shop;

public abstract class AbsCrawlShop {
	/**
	 * 商品主页url列表
	 * @return
	 * @throws IOException
	 */
	public abstract List<String> getShopUrlList()throws IOException;
	
	public abstract List<String> getShopUrlList(String url)throws IOException;
	/**
	 * 自动方式获取商品
	 * @return
	 * @throws IOException
	 */
	public List<Shop> getShopList()throws IOException{
		return getShopList(getShopUrlList());
	}
	
	
	/**
	 * 商品列表
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public List<Shop> getShopList(List<String> url)throws IOException{
		List<Shop>  list = new ArrayList<>();
		for(String u:url){
			Shop s = getShop(u);
			if(s==null){
				continue;
			}
			list.add(s);
		}
		return list;
	}
	
	/**
	 * 获取商品
	 * @param url
	 * @return
	 * @throws IOException
	 */
	protected abstract Shop getShop(String url)throws IOException;
	
	
	/**
	 * 商品的评论
	 * @param url
	 * @return
	 * @throws IOException
	 */
	protected abstract List<Comment> getComment(String url)throws IOException;
}
