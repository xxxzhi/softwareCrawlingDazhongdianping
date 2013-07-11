package com.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.crawl.dianping.AbsCrawlShop;
import com.crawl.dianping.CrawlShopUrlListFromDianPing;
import com.model.Comment;
import com.model.Shop;

public class DBIn {
	Connection connection ;
	
	public DBIn(){
		connection = DBConnection.getConnection();
		
	}
	public void close(){
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void insertShop(Shop shop){
		if(shop==null)
			return;
		try{
			Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			
			ResultSet rsSearch  = stmt.executeQuery("select * from shop where name='"+shop.getName()+"'");
			
			System.out.println(rsSearch.getRow());
			if(rsSearch.getRow()>0){
				rsSearch.close();
				stmt.close();
				return ;
			}
			rsSearch.close();
			ResultSet rsS   = stmt.executeQuery("select max(shop_id) as maxid from shop");
			rsS.last();
			int shopId = rsS.getInt("maxid")+1;
			PreparedStatement pstmt = connection.prepareStatement("insert into shop(topic,name,image,addr,tel,avg_cost,url,shop_id) " +
					"values(?,?,?,?,?,?,?,?)");
			pstmt.setString(1, shop.getType());
			pstmt.setString(2, shop.getName());
			pstmt.setString(3, shop.getImg());
			pstmt.setString(4, shop.getAddr());
			pstmt.setString(5, shop.getTel());
			if(shop.getAvgCost().length()>1)
				pstmt.setInt(6,  Integer.valueOf(shop.getAvgCost()));
			else{
				pstmt.setInt(6, -1);
			}
			pstmt.setString(7, shop.getUrl());
			pstmt.setInt(8,shopId );
			pstmt.executeUpdate();
			pstmt.close();
//			System.out.println("shopId:"+shopId);
			
			
			ResultSet rsComment = stmt.executeQuery("select comment_id,shop_id,content,service_rate,envir_rate,taste_rate from comment where comment_id is null");			//comment 表
			for(Comment comment:shop.getListComment()){
				rsComment.moveToInsertRow();
				rsComment.updateInt("shop_id",shopId);
				rsComment.updateNString("content", comment.getContent());
				rsComment.updateInt("service_rate",comment.getService_rate());
				rsComment.updateInt("envir_rate", comment.getEnvir_rate());
				rsComment.updateInt("taste_rate", comment.getTaste_rate());
				rsComment.insertRow();
			}
			rsComment.close();
			
			stmt.close();
			
		}catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void test(){
		Statement stmt;
		try {
			stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		
			ResultSet rsSearch   = stmt.executeQuery("select max(shop_id) as maxid from shop");
			rsSearch.last();
			System.out.println(rsSearch.getInt("maxid"));
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		new DBIn().test();
		
	}

}
