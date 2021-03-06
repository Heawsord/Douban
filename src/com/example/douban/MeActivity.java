package com.example.douban;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MeActivity extends Activity implements OnItemClickListener{

	private ListView mListView;
	private SharedPreferences sp;
	private static String[] arr={"我读","我看","我听","我评","我的日记","我的资料","小组"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		//在清单文件里设置了隐藏标题栏
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fav);
		sp=getSharedPreferences("config",Context.MODE_PRIVATE);
		mListView=(ListView) this.findViewById(R.id.mylistview);
		//this.findViewById();
		mListView.setAdapter(new ArrayAdapter<String>(this,R.layout.fav_item,R.id.fav_title,arr));
		//注册listview点击事件
		
		mListView.setOnItemClickListener(this);
	
	}
	/**
	 * 判断用户是否获取到了授权
	 * @return
	 */
	private boolean isUserAuthorized(){
		
		String accesstoken=sp.getString("accesstoken","");
		String tokensecret=sp.getString("tokensercret","");
		
		if(accesstoken==null||tokensecret==null||"".equals(tokensecret)||"".equals(tokensecret)){
			
			return false;
			
		} else{
			
			return true;
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		if(isUserAuthorized()){
			switch(position){
			case 0:
				Intent myReadIntent=new Intent(MeActivity.this,MyReadActivity.class);
				startActivity(myReadIntent);
				break;
			case 5:
				Intent myInfoIntent=new Intent(MeActivity.this,MyInfoActivity.class);
				startActivity(myInfoIntent);
				break;
			default:
				break;
			}
		} else{
			//定向到登录界面
			Intent intent =new Intent(this,LoginInActivity.class);
			startActivity(intent);
		}
		
	}
	


	
}
