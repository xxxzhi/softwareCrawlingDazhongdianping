package com.crawl.dianping;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.db.DBIn;
import com.model.Comment;
import com.model.Shop;

public class CrawlShopUrlListFromDianPing extends AbsCrawlShop {
	private static String url = "http://www.dianping.com/search/keyword/4/0_%E5%A4%A7%E5%AD%A6%E5%9F%8E/";
	
	private String headUrl = "http://www.dianping.com";
	@Override
	public List<String> getShopUrlList() throws IOException  {
		// TODO Auto-generated method stub
		return getShopUrlList(url);
	}
	
	@Override
	protected Shop getShop(String url) throws IOException {
		Document doc = Jsoup.connect(url).header("User-Agent", 
				"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2").timeout(5000).get();
//		System.out.println(doc);
		Elements elements = doc.select("div[class=shop-wrap] div[class=main] div[class=shop-info shakeable] " +
				"div[class=shop-info-con]");
		if(elements.size()<1){
			System.out.println("error:"+url);
			writeErrorUrl(url);
			return null;
		}
		Element shopInfo = elements.get(0);
		
		Shop shop = new Shop();
		
		Elements title = shopInfo.select("div[class=shop-tit] div[class=shop-name] h1[class=shop-title] ");
//		System.out.println(title.get(0).text());
		shop.setName(title.get(0).text());
		
		Elements cost = shopInfo.select("div[class=shop-tit] div[class=comment-rst] span strong[class=stress] ");
		String cost1 = cost.get(0).text();
//		System.out.println(cost1.substring(1));
		shop.setAvgCost(cost1.substring(1));
		
		
		Elements pic = shopInfo.select("div[class=pic-txt]  div[class=pic] div[class=thumb-switch] ul li a img");
//		System.out.println(pic.get(0).attr("src"));
		String picUrl = "";
		picUrl+=pic.get(0).attr("src");
		shop.setImg(picUrl);
		
		Elements addr = shopInfo.select("div[class=pic-txt] div[class=txt] div[class=shop-location] ul li span[itemprop=street-address]");
		shop.setAddr(addr.get(0).text().trim());
		
		Elements tel = shopInfo.select("div[class=pic-txt] div[class=txt] div[class=shop-location] ul li span[itemprop=tel]");
		shop.setTel(tel.get(0).text());
		
		shop.setUrl(url);
		
		Elements type = doc.select("div[class=breadcrumb] b a span[class=bread-name]");
//		System.out.println(type.get(3).text());
		shop.setType(type.get(3).text());
		
		
		shop.setListComment(getComment(url));
		
		return shop;
	}

	@Override
	protected List<Comment> getComment(String url) throws IOException {
		// TODO Auto-generated method stub
		Document doc = Jsoup.connect(url).header("User-Agent", 
				"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2").timeout(5000).get();
		Elements elements = doc.select("div[class=main page-sa page-shop Fix] div[class=section] div[class=block raw-block separated-block shop-comment] " +
				"div[class=show-more] a");
		String moreUrl =headUrl+ elements.get(1).attr("href");
		if(moreUrl.length()<3){
			System.out.println("comment error:"+url+"      and moreUrl is "+moreUrl);
			writeErrorUrl("errorCommenturl.txt",url);
			return new ArrayList<Comment>();
		}
//		System.out.println("moreUrl:"+moreUrl);
		
		doc = Jsoup.connect(moreUrl).header("User-Agent",
				"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2").get();
		elements = doc.select("div[class=shop-wrap shop-revitew] div[class=main] div[class=shop-comment all-comment revitew-comm] " +
				"div[class=comment-mode] "+
				"div[class=comment-list] ul li");
		List<Comment> list = new ArrayList<>();
		for(int i=0;i!=elements.size();++i){
			Element e = elements.get(i);
			Comment comment = new Comment();
			comment.setContent(e.select("li[id] ").select("div[class=content] div[class=comment-txt]").text());
			if(comment.getContent().length()<1){
				continue;
			}
			Elements e1s = e.select("li[id] ").select("div[class=content] div[class=user-info] div[class=comment-rst] span[class=rst] em[class=col-exp] ");
			
			if(e1s.size() == 3){
				String taste_rate= e1s.get(0).text();
				String service_rate= e1s.get(1).text();
				String envir_rate= e1s.get(2).text();
				taste_rate = taste_rate.substring(1, taste_rate.length()-1);
				service_rate = service_rate.substring(1, service_rate.length()-1);
				envir_rate = envir_rate.substring(1, envir_rate.length()-1);
				
				
				comment.setService_rate(trasRate(service_rate));
				comment.setTaste_rate(trasRate(taste_rate));
				comment.setEnvir_rate(trasRate(envir_rate));
			}else{
				comment.setService_rate(-1);
				comment.setTaste_rate(-1);
				comment.setEnvir_rate(-1);
			}
//			System.out.println(comment);
			list.add(comment);
		}
		
		return list;
	}
	private int trasRate(String star){
		int starInt = -1;
		if(star.equals("非常好")){
			starInt =4;
		}else if(star.equals("很好")){
			starInt =3;
		}else if(star.equals("好")){
			starInt =2;
		}else if(star.equals("一般")){
			starInt =1;
		}else if(star.equals("差")){
			starInt =0;
		}
		return starInt;
	}
	@Override
	public List<String> getShopUrlList(String url) throws IOException {
		Document doc = Jsoup.connect(url).header("User-Agent", 
				"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2").timeout(5000).get();
		Elements elements = doc.select("div[class=main page-asa Fix] div[class=section] div[class=section-inner] " +
				"div[class=box searchList searchResult searchList-v1009 listModeView] "+
				"dl dd ul[class=detail] li[class=shopname] a[class=BL] ");
		
//		System.out.println(elements);
		List<String> list = new ArrayList<String>();
		
		for(Element element:elements){
			list.add(headUrl+element.attr("href"));
		}
		
		return list;
	}
	public static void main(String[] args) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		CrawlShopUrlListFromDianPing crawlList = new CrawlShopUrlListFromDianPing();
		DBIn in = new DBIn();
		int counts = 0;
		int Nums = 0;
		
//		readUrl(crawlList);
		
//		readShop(crawlList,in);
		
		readErrorShop(crawlList,in);
		
//		System.out.println(crawlList.getErrorShop("http://www.dianping.com/shop/2549377"));
	}

	
	public static void readUrl(AbsCrawlShop crawlList) throws InterruptedException{
		int counts = 0;
		int Nums = 0;
		File file = new File("url.txt");
		try {
			RandomAccessFile accessFile = new RandomAccessFile(file, "rw");
			accessFile.seek(file.length());
			for(int i=26;i!=51;++i){
				String url1 = url+"p"+i;
				List<String> urls = crawlList.getShopUrlList(url1);
				for(int j=0;j!=urls.size();++j){
					if(counts<Nums){
						continue;
					}
					String url = urls.get(j);
					
					accessFile.writeUTF(url.trim());
				
					counts++;
					System.out.println("counts:"+counts);
//					Shop shop = crawlList.getShop(url);
////					System.out.println(shop);
//					if(shop==null){
//						continue;
//					}
//					in.insertShop(shop);
				}
				TimeUnit.SECONDS.sleep(3);
			}
			accessFile.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void readErrorShop(CrawlShopUrlListFromDianPing crawlList,DBIn in) throws InterruptedException{
		File file = new File("errorurl.txt");
//		readUrl(crawlList);
		int counts = 0;
		int Nums = 0;
		List<String> list =new ArrayList<>();
		try {
			RandomAccessFile accessFile = new RandomAccessFile(file, "rw");
			try {
				while(true){
					counts++;
					if(counts<Nums){
						continue;
					}
					System.out.println("counts:"+counts);
					String url = accessFile.readUTF();
					if(list.contains(url)){
						continue;
					}
					list.add(url);
					
					System.out.print(url+"  ");
					boolean isOk = true;
					Shop shop=null;
					do{
						isOk = true;
						try{
							System.out.println("crawl:"+url);
							shop = crawlList.getErrorShop(url);
						}catch (java.net.SocketTimeoutException e) {
							System.out.println("socketTimeOutException sleep for 30");
							TimeUnit.SECONDS.sleep(30);
							isOk = false;
						}catch(Exception e){
							shop = null;
							
						}
					}while(!isOk);
					in.insertShop(shop);
					System.out.println("ok");
					TimeUnit.SECONDS.sleep(4);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		in.close();
	}
	
	public static void readShop(AbsCrawlShop crawlList,DBIn in) throws InterruptedException{
		File file = new File("url.txt");
//		readUrl(crawlList);
		int counts = 0;
		int Nums = 708;
		try {
			RandomAccessFile accessFile = new RandomAccessFile(file, "rw");
			try {
				while(true){
					counts++;
					if(counts<Nums){
						continue;
					}
					System.out.println("counts:"+counts);
					String url = accessFile.readUTF();
					if(url.equals("http://www.dianping.com/shop/7998103")){
						continue;
					}
					System.out.print(url+"  ");
					boolean isOk = true;
					Shop shop=null;
					do{
						isOk = true;
						try{
							System.out.println("crawl:"+url);
							shop = crawlList.getShop(url);
						}catch (java.net.SocketTimeoutException e) {
							System.out.println("socketTimeOutException sleep for 30");
							TimeUnit.SECONDS.sleep(30);
							isOk = false;
						}
					}while(!isOk);
					in.insertShop(shop);
					System.out.println("ok");
					TimeUnit.SECONDS.sleep(4);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		in.close();
	}
	
	
	public void writeErrorUrl(String url){
		File file = new File("errorurl.txt");
			RandomAccessFile accessFile;
			try {
				accessFile = new RandomAccessFile(file, "rw");
			
				accessFile.seek(file.length());
				accessFile.writeUTF(url.trim());
			accessFile.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	
	public void writeErrorUrl(String name,String url){
		File file = new File(name);
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			RandomAccessFile accessFile;
			try {
				accessFile = new RandomAccessFile(file, "rw");
			
				accessFile.seek(file.length());
				accessFile.writeUTF(url.trim());
			accessFile.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	protected Shop getErrorShop(String url) throws IOException {
		Document doc = Jsoup.connect(url).header("User-Agent", 
				"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2").timeout(5000).get();
//		System.out.println(doc);
		Elements elements = doc.select("div[class=main page-sa page-shop Fix] div[class=section] div[class=block shop-info] " +
				"div[class=shop-info-inner Fix] ");
		if(elements.size()<1){
			System.out.println("error:"+url);
			writeErrorUrl("errorurl2",url);
			return null;
		}
		Element shopInfo = elements.get(0);
		
		Shop shop = new Shop();
		
		Elements title = shopInfo.select("div[class=shop-name] h1[class=shop-title] ");
		System.out.println(title.get(0).text());
		shop.setName(title.get(0).text());
		
		Elements cost = shopInfo.select("div[class=comment-rst] dl dd ");
		String cost1 = cost.get(0).text();
		System.out.println(cost1.substring(1));
		shop.setAvgCost(cost1.substring(1));
		
		
		Elements pic = shopInfo.select("div[class=shop-gallery] div[class=thumb-wrapper] a img");
		System.out.println(pic.get(0).attr("src"));
		String picUrl = "";
		picUrl+=pic.get(0).attr("src");
		shop.setImg(picUrl);
		
		Elements addr = shopInfo.select("div[class=desc-list] dl[class=shopDeal-Info-address] dd span");
		shop.setAddr(addr.get(0).text().trim());
		
//		Elements tel = shopInfo.select("div[class=pic-txt] div[class=txt] div[class=shop-location] ul li span[itemprop=tel]");
		shop.setTel("");
		
		shop.setUrl(url);
		
		Elements type = doc.select("div[class=breadcrumb] b a span[class=bread-name]");
//		System.out.println(type.get(3).text());
		shop.setType(type.get(3).text());
		
		
		shop.setListComment(getComment(url));
		
		return shop;
	}
	
}
