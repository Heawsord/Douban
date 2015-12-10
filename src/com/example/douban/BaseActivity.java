package com.example.douban;

import com.google.gdata.client.douban.DoubanService;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.*;

public abstract class BaseActivity extends Activity {

	public TextView mTextViewTitle;
	public RelativeLayout mRelativeLoading;
	public ImageButton mImageBack;
	public DoubanService myService;
	public abstract void setupView();
	public abstract void setListener();
	public abstract void fillData();

	/**
	 * 显示加载数据布局
	 */
	public void showLoading() {
		mRelativeLoading.setVisibility(View.VISIBLE);
		AlphaAnimation aa=new AlphaAnimation(0.0f,1.0f);
		aa.setDuration(1000);
		ScaleAnimation sa=new ScaleAnimation(0.0f,1.0f,0.0f,1.0f);
		sa.setDuration(1000);
		AnimationSet animationSet=new AnimationSet(false);
		animationSet.addAnimation(aa);
		animationSet.addAnimation(sa);
		mRelativeLoading.setAnimation(animationSet);
		mRelativeLoading.startAnimation(animationSet);

	}
	/**
	 * 隐藏加载数据布局
	 */
	public void hideLoading() {
		mRelativeLoading.setVisibility(View.VISIBLE);
		AlphaAnimation aa=new AlphaAnimation(1.0f,0.0f);
		aa.setDuration(1000);
		ScaleAnimation sa=new ScaleAnimation(1.0f,0.0f,1.0f,1.0f);
		sa.setDuration(1000);
		AnimationSet animationSet=new AnimationSet(false);
		animationSet.addAnimation(aa);
		animationSet.addAnimation(sa);
		mRelativeLoading.setAnimation(animationSet);
		mRelativeLoading.startAnimation(animationSet);
		mRelativeLoading.setVisibility(View.INVISIBLE);

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		String apiKey=getResources().getString(R.string.apikey);
		String apiSecret=getResources().getString(R.string.secret);
		myService=new DoubanService("黑马小瓣瓣",apiKey,apiSecret);
		//把密钥从sharedPreferences中取出设置给豆瓣service
		SharedPreferences sp=getSharedPreferences("config",Context.MODE_PRIVATE);
		String accesstoken=sp.getString("accesstoken", "");
		String tokensecret=sp.getString("tokensercret", "");
		myService.setAccessToken(accesstoken, tokensecret);
	}
	
	public void showToast(String text){
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
	
}
