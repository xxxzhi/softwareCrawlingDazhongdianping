package com.model;

public class Comment {
	public int getService_rate() {
		return service_rate;
	}
	public void setService_rate(int service_rate) {
		this.service_rate = service_rate;
	}
	public int getEnvir_rate() {
		return envir_rate;
	}
	public void setEnvir_rate(int envir_rate) {
		this.envir_rate = envir_rate;
	}
	public int getTaste_rate() {
		return taste_rate;
	}
	public void setTaste_rate(int taste_rate) {
		this.taste_rate = taste_rate;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return 	id+","+shopId+","+content+","+service_rate+","+envir_rate+","+taste_rate+"\n";
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	private String id;
	private String shopId;
	private int service_rate; 
	private int envir_rate;
	private int taste_rate;
	private String content;
	
	
}
