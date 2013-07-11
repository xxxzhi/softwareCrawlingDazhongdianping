package com.model;

import java.util.List;

public class Shop {
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return 	 type+","+id+","+name+","+img+","+addr+","+tel+","+url+",\n"+listComment;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the listComment
	 */
	public List<Comment> getListComment() {
		return listComment;
	}

	/**
	 * @param listComment the listComment to set
	 */
	public void setListComment(List<Comment> listComment) {
		this.listComment = listComment;
	}

	/**
	 * @return the avgCost
	 */
	public String getAvgCost() {
		return avgCost;
	}

	/**
	 * @param avgCost the avgCost to set
	 */
	public void setAvgCost(String avgCost) {
		this.avgCost = avgCost;
	}

	private String type;
	private String id;
	private String name;
	
	private String avgCost = "";
	/**
	 * 用逗号分开
	 */
	private String img;
	private String addr;
	
	
	/**
	 * 用逗号分开多个
	 */
	private String tel;
	
	private String url;
	
	
	private List<Comment> listComment;
	
	
}
