package com.example.douban;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyInfoActivity extends BaseActivity implements OnClickListener{
	private ImageView userImage;
	private TextView userName;
	private TextView userAddress;
	private TextView userDescription;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_info);
	}

	@Override
	public void setupView() {
		userImage=(ImageView) this.findViewById(R.id.imgUser);
		userName=(TextView) this.findViewById(R.id.txtUserName);
		userAddress=(TextView) this.findViewById(R.id.txtUserAddress);
		userDescription=(TextView) this.findViewById(R.id.txtUserDescription);
		mRelativeLoading=(RelativeLayout) this.findViewById(R.id.loading);
		mTextViewTitle=(TextView) this.findViewById(R.id.my_title);
		mImageBack=(Button) this.findViewById(R.id.back_button);
		

	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void fillData() {
		mRelativeLoading.setVisibility(View.VISIBLE);
		

	}

	@Override
	public void showLoading() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hideLoading() {
		// TODO Auto-generated method stub

	}

	@Override
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
