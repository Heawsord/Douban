package com.example.douban.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.gdata.client.douban.DoubanService;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.douban.NoteEntry;
import com.google.gdata.util.ServiceException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

//�����������ص�����
public class NetUtil {

	public static String isNeedCaptcha() throws Exception{
		URL url=new URL("http://www.douban.com/accounts/login");
		URLConnection conn=url.openConnection();
		Source source=new Source(conn);
		List<Element> elements= source.getAllElements("input");
		for(Element element:elements){
			String result=element.getAttributeValue("name");
			if("captcha-id".equals(result)){
				
				return element.getAttributeValue("value");
			}
		}
		return null;
	}
	
	public static Bitmap getImage(String path)throws Exception{
		
		URL url=new URL(path);
		HttpURLConnection conn=(HttpURLConnection) url.openConnection();
		InputStream in=conn.getInputStream();
		return BitmapFactory.decodeStream(in);
	}
	
	public static boolean login(String name,String pwd,String captcha,String captchaId,Context context) throws Exception{
		
		String apiKey = "0c51c1ba21ad8cfd24f5452e6508a6f7";
		String secret = "359e16e5e5c62b6e";

		DoubanService myService = new DoubanService("����С���", apiKey,
				secret);

		Log.d("NetUtil","please paste the url in your webbrowser, complete the authorization then come back:");
		String url =myService.getAuthorizationUrl(null);
		Log.d("NetUtil",url);
		//System.out.println(url);
		/*byte buffer[] = new byte[1];
		try {
			System.in.read(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//��ȡ����Ȩ�������ƺ���Կ
		ArrayList<String> tokens=myService.getAccessToken();
		System.out.println(tokens.get(0));
		System.out.println(tokens.get(1));
		//myService.setAccessToken("1ffed0067cd19f075ffc70137674dd15", "37405c70e93c6c7c");
		//testWriteNoteEntry(myService);*/
		//��httpClientģ���û���¼����
		//��httpClient����https://www.douban.com/accounts/login �����¼�ɹ���cookie
		DefaultHttpClient client=new DefaultHttpClient();
		HttpPost post=new HttpPost("http://www.douban.com/accounts/login");
		List<NameValuePair> pairs=new ArrayList<NameValuePair>();
		//pairs.add(new BasicNameValuePair("ck","hWJ8"));
		pairs.add(new BasicNameValuePair("source","None"));
		pairs.add(new BasicNameValuePair("redir","www.douban.com"));
		pairs.add(new BasicNameValuePair("form_email",name));
		pairs.add(new BasicNameValuePair("form_password",pwd));
		pairs.add(new BasicNameValuePair("captcha-solution",captcha));
		pairs.add(new BasicNameValuePair("captcha-id",captchaId));
		pairs.add(new BasicNameValuePair("login","��¼"));
		UrlEncodedFormEntity entity=new UrlEncodedFormEntity(pairs,"utf-8");
		post.setEntity(entity);
		HttpResponse response=client.execute(post);
		Log.d("NetUtil",response.getStatusLine().getStatusCode()+"");
		//System.out.println(response.getStatusLine().getStatusCode());
		Source source=new Source(response.getEntity().getContent());
		//System.out.println(source.toString());
		Log.d("NetUtil","�鿴��¼����"+source.toString());
		
		//��ȡ��¼�ɹ���cookies
		CookieStore cookies=client.getCookieStore();
		//����cookies���ʶ�����֤��վ
		
		//����cookies���ʶ�����֤��վ
		//ģ���û���Ȩ����
		DefaultHttpClient client2=new DefaultHttpClient();
		HttpPost post1=new HttpPost(url);
		String oauth_token =url.substring(url.lastIndexOf("=")+1, url.length());
		System.out.println(oauth_token);
		List<NameValuePair> namevaluepairs1  = new ArrayList<NameValuePair>();
	    namevaluepairs1.add(new BasicNameValuePair("ck","-RKb"));
	    namevaluepairs1.add(new BasicNameValuePair("oauth_token",oauth_token));
	    namevaluepairs1.add(new BasicNameValuePair("oauth_callback",""));
	    namevaluepairs1.add(new BasicNameValuePair("ssid","0cbb188a"));
	    namevaluepairs1.add(new BasicNameValuePair("confirm","ͬ��"));
		UrlEncodedFormEntity entity1=new UrlEncodedFormEntity(namevaluepairs1,"utf-8");
		post1.setEntity(entity1);

		client2.setCookieStore(cookies);
		HttpResponse response2=client2.execute(post1);
		//Source source1=new Source(response2.getEntity().getContent());
		//System.out.println(source1.toString());
		
		//��ȡ����Ȩ�������ƺ���Կ
		ArrayList<String> tokens=myService.getAccessToken();
		String accesstoken=tokens.get(0);
		String tokensecret=tokens.get(1);
		Log.d("NetUtil","���ƣ�"+accesstoken);
		Log.d("NetUtil","��Կ��"+tokensecret);
		//�����ƺ���Կ����SharedPreferences��
		SharedPreferences sp=context.getSharedPreferences("config",context.MODE_PRIVATE);
		Editor editor=sp.edit();
		editor.putString("accesstoken", accesstoken);
		editor.putString("tokensercret", tokensecret);
		editor.commit();
		return true;
				
		
	}
}
