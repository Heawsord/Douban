package com.example.douban;

import java.io.IOException;
import java.util.List;

import com.example.douban.util.LoadImageAsyncTask;
import com.example.douban.util.LoadImageAsyncTask.LoadImageAsyncTaskCallback;
import com.google.gdata.data.Link;
import com.google.gdata.data.TextContent;
import com.google.gdata.data.douban.UserEntry;
import com.google.gdata.util.ServiceException;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyInfoActivity extends BaseActivity implements OnClickListener{
	private ImageView userImage;
	private TextView userName;
	private TextView userAddress;
	private TextView userDescription;
	String name;
	String location;
	String content;
	String iconUrl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		setContentView(R.layout.my_info);
		super.onCreate(savedInstanceState);
		setupView();
		setListener();
		fillData();
	}

	@Override
	public void setupView() {
		userImage=(ImageView) this.findViewById(R.id.imgUser);
		userName=(TextView) this.findViewById(R.id.txtUserName);
		userAddress=(TextView) this.findViewById(R.id.txtUserAddress);
		userDescription=(TextView) this.findViewById(R.id.txtUserDescription);
		mRelativeLoading=(RelativeLayout) this.findViewById(R.id.loading);
		mTextViewTitle=(TextView) this.findViewById(R.id.my_title);
		mImageBack=(ImageButton) this.findViewById(R.id.back_button);
		

	}

	@Override
	public void setListener() {
		mImageBack.setOnClickListener(this);

	}

	@Override
	public void fillData() {
		
		//������ݱȽϺ�ʱ �������߳���ִ��;
		new AsyncTask<Void,Void,Void>(){

			//���첽����֮ǰִ�еķ����������߳���ִ�еķ���
			@Override
			protected void onPreExecute() {
				showLoading();
				super.onPreExecute();
			}

			//���첽����ִ��֮��֮����õķ���
			@Override
			protected void onPostExecute(Void result) {
				hideLoading();
				super.onPostExecute(result);
				userName.setText(name);
				userAddress.setText(location);
				userDescription.setText(content);
				//�����û�ͷ��
				LoadImageAsyncTask task=new LoadImageAsyncTask(new LoadImageAsyncTaskCallback() {
					
					@Override
					public void beforeLoadImage() {
						//��ͷ������Ĭ��ͼ��
						userImage.setImageResource(R.drawable.launcher);
						
					}
					
					@Override
					public void afterLoadImage(Bitmap bitmap) {
						if(bitmap!=null){
							userImage.setImageBitmap(bitmap);
						} else{
							userImage.setImageResource(R.drawable.ic_launcher);
						}
						
					}
				});
				task.execute(iconUrl);
			}

			//�ں�ִ̨�еķ���
			@Override
			protected Void doInBackground(Void... params) {
				UserEntry ue;
				try {
					ue = myService.getAuthorizedUser();
					name =ue.getTitle().getPlainText();
					location =ue.getLocation();
					content=((TextContent) ue.getContent()).getContent().getPlainText();
					List<Link> links=ue.getLinks();
					for(Link link:links){
						if("icon".equals(link.getRel())){
							iconUrl=link.getHref();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}}.execute();
		
		
		
		

	}

	public void onClick(View v) {
		switch(v.getId()){
		case R.id.back_button:
			finish();
			break;
		default:
			break;
		}
		
	}

}
