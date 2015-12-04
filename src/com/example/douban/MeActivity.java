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
	private static String[] arr={"�Ҷ�������","�ҿ�������","����������","����������","�ҵ��ռ�","�ҵ�����","С��"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fav);
		sp=getSharedPreferences("config",Context.MODE_PRIVATE);
		mListView=(ListView) this.findViewById(R.id.mylistview);
		//this.findViewById();
		mListView.setAdapter(new ArrayAdapter<String>(this,R.layout.fav_item,R.id.fav_title,arr));
		//ע��listview����¼�
		
		mListView.setOnItemClickListener(this);
	
	}
	/**
	 * �ж��û��Ƿ��ȡ������Ȩ
	 * @return
	 */
	private boolean isUserAuthorized(){
		
		String accesstoken=sp.getString("accesstoken", null);
		String tokensecret=sp.getString("tokensecret", null);
		
		if(accesstoken==null||tokensecret==null||"".equals(tokensecret)||"".equals(tokensecret)){
			
			return false;
			
		} else{
			
			return true;
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if(isUserAuthorized()){
			//
		} else{
			//���򵽵�¼����
			Intent intent =new Intent(this,LoginInActivity.class);
			startActivity(intent);
		}
		
	}
	


	
}
