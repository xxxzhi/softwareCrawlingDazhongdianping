package com.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.crawl.dianping.AbsCrawlShop;
import com.crawl.dianping.CrawlShopUrlListFromDianPing;
import com.db.DBConnection;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AbsCrawlShop crawlList = new CrawlShopUrlListFromDianPing();
		try {
			
			System.out.println(crawlList.getShopList());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
//		if(DBConnection.getConnection()!=null){
//			System.out.println("success");
//		}else{
//			System.out.println("fail");
//		}
	}

}
