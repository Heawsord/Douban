package com.example.douban;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class MainTabActivity extends TabActivity {

	private TabHost mTabHost;
	private LayoutInflater inflater;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main_tab);
		//对layoutInflater进行初始化;
		inflater=LayoutInflater.from(this);
		mTabHost=getTabHost();
		mTabHost.addTab(getNewBookTab());
		mTabHost.addTab(getMyDoubanTab());
		mTabHost.addTab(getCommentsTab());
		mTabHost.addTab(getSearchTab());
		mTabHost.addTab(getAboutTab());
		mTabHost.setCurrentTabByTag("newbook");

		
	}
	
	private TabSpec getNewBookTab(){
		TabSpec spec=mTabHost.newTabSpec("newbook");
		Intent intent =new Intent(this,TestActivity.class);
		spec.setContent(intent);
		Drawable icon =getResources().getDrawable(R.drawable.ic_launcher);
		//spec.setIndicator("豆瓣新书",icon);
		spec.setIndicator(getIndicatorView("豆瓣新书",R.drawable.tab_main_nav_book));
		return spec;
		
		
	}
	private TabSpec getMyDoubanTab(){
		TabSpec spec=mTabHost.newTabSpec("myDouban");
		Intent intent =new Intent(this,MeActivity.class);
		spec.setContent(intent);
		Drawable icon =getResources().getDrawable(R.drawable.ic_launcher);
		spec.setIndicator(getIndicatorView("我的豆瓣",R.drawable.tab_main_nav_me));
		return spec;
		
		
	}
	
	private TabSpec getCommentsTab(){
		
		TabSpec spec=mTabHost.newTabSpec("comments");
		Intent intent =new Intent(this,TestActivity1.class);
		spec.setContent(intent);
		spec.setIndicator(getIndicatorView("书评",R.drawable.detail_comment_selected));
		return spec;
	}
	
	private TabSpec getSearchTab(){
		
		TabSpec spec=mTabHost.newTabSpec("Search");
		Intent intent =new Intent(this,TestActivity1.class);
		spec.setContent(intent);
		spec.setIndicator(getIndicatorView("搜索",R.drawable.tab_main_nav_search));
		return spec;
	}
	
	private TabSpec getAboutTab(){
		
		TabSpec spec=mTabHost.newTabSpec("About");
		Intent intent =new Intent(this,TestActivity1.class);
		spec.setContent(intent);
		spec.setIndicator(getIndicatorView("关于",R.drawable.tab_main_nav_more));
		return spec;
	}
	
	@SuppressLint("InflateParams") private View getIndicatorView(String name, int iconId){
		View view=inflater.inflate(R.layout.tab_main_nav, null);
		ImageView ivicon=(ImageView) view.findViewById(R.id.ivIcon);
		TextView tvtitle=(TextView) view.findViewById(R.id.tvTitle);
		ivicon.setImageResource(iconId);
		tvtitle.setText(name);
		return view;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater=new MenuInflater(this);
		inflater.inflate(R.menu.main_tab_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.main_tab_menu_clear_user:
			SharedPreferences sp=getSharedPreferences("config",Context.MODE_PRIVATE);
			Editor editor=sp.edit();
			editor.putString("accesstoken","");
			editor.putString("tokensercret", "");
			editor.commit();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
