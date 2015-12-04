package com.example.douban;

import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import com.example.douban.util.NetUtil;

public class NetTest {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		URL url=new URL("www.douban.com/accounts/login");
		URLConnection conn=url.openConnection();
		Source source=new Source(conn);
		List<Element> elements= source.getAllElements("input");
		for(Element element:elements){
			String result=element.getAttributeValue("name");
			if("captcha-id".equals(result)){
				
				System.out.println("需要验证码");
				
			}
		}
		System.out.println("不需要验证码");
		

	}

}
