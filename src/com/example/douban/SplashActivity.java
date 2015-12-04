package com.example.douban;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {

	private TextView versionNum;
	private LinearLayout myLinearLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		versionNum=(TextView)this.findViewById(R.id.versionNumber);
		versionNum.setText(getVersion());
		myLinearLayout=(LinearLayout) this.findViewById(R.id.linearLayout01);
		Log.d("SplashActivity","___________________________________");
		//�жϵ�ǰ����״̬�Ƿ����
		if(isNetworkConnected()) {
			Toast.makeText(this, "�������", Toast.LENGTH_SHORT).show();
			Log.d("SplashActicity","�������");
			//����һ���볡����
			AlphaAnimation aa=new AlphaAnimation(0.0f, 1.0f);
			aa.setDuration(2000);
			myLinearLayout.setAnimation(aa);
			myLinearLayout.startAnimation(aa);
			//�ӳ�ִ��r����
			new Handler().postDelayed(new LoadMainTaskTab(), 2000);
			
			
		} else{
			//AlertDialog.Builder dialog=new Builder();
			Toast.makeText(this, "����not����", Toast.LENGTH_SHORT).show();
			Log.d("SplashActivity","___________________________________���粻����");
			showNetworkDialog();
		}
		
		
	}

	private String getVersion(){
		try {
			PackageInfo info=getPackageManager().getPackageInfo(getPackageName(), 0);
			return "Version" +info.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Vesion";
		}
	}
	/**
	 * �ж�����״̬
	 * @return ��������״̬�Ĳ���ֵ
	 */
	private boolean isNetworkConnected(){
		ConnectivityManager manager=(ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo info=manager.getActiveNetworkInfo();
		//�ж�wifi�Ƿ����
		//WifiManager wifiManager =(WifiManager) getSystemService(WIFI_SERVICE);
		//wifiManager.isWifiEnabled();
		//wifiManager.getWifiState();
		return (info!=null&&info.isConnected());
	}
	
	/**
	 * ��ʾ���粻���öԻ���
	 */
	private void showNetworkDialog(){
		AlertDialog.Builder builder=new Builder(this);
		builder.setTitle("��������")
		.setMessage("�������������������")
		.setPositiveButton("����", new OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which){
				Intent intent=new Intent();
				//�ڶ�������Ҫ��ȫ��
				intent.setClassName("com.android.settings", "com.android.settings.Settings");
				startActivity(intent);
				finish();
			}
		})
		.setNegativeButton("ȡ��",new OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog,int which){
				finish();
			}
		})
		.show();
	}
	
	/**
	 * ����������
	 */
	private class LoadMainTaskTab implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Intent intent =new Intent(SplashActivity.this,MainTabActivity.class);
			startActivity(intent);
			finish();
			
		}}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
