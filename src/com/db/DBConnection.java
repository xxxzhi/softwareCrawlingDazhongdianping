package com.db;


import java.sql.Connection;
import java.sql.DriverManager;


public class DBConnection {
	public static Connection getConnection(){
		Connection con = null;
		try{
			String Driver="com.mysql.jdbc.Driver";    //驱动程序 
		    String URL="jdbc:mysql://125.216.245.213:3306/duanping";    //连接的URL,db_name为数据库名    
		    String Username="root";    //用户名
		    String Password="root";    //密码
		    Class.forName(Driver);
		    con=DriverManager.getConnection(URL, Username, Password);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return con;
	}
}
