package com.example.douban;

import com.google.gdata.client.douban.DoubanService;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;

public abstract class BaseActivity extends Activity {

	public TextView mTextViewTitle;
	public RelativeLayout mRelativeLoading;
	public Button mImageBack;
	public DoubanService doubanService;
	public abstract void setupView();
	public abstract void setListener();
	public abstract void fillData();
	public abstract void showLoading();
	public abstract void hideLoading();
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		String apiKey=getResources().getString(R.string.apikey);
		String apiSecret=getResources().getString(R.string.secret);
		doubanService=new DoubanService("黑马小瓣瓣",apiKey,apiSecret);
		//把密钥设置给豆瓣service
		SharedPreferences sp=getSharedPreferences("config",Context.MODE_PRIVATE);
		String accesstoken=sp.getString("accesstoken", "");
		String tokensecret=sp.getString("tokensecret", "");
		doubanService.setAccessToken(accesstoken, tokensecret);
	}
	
}
