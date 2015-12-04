package com.example.douban;

import com.example.douban.util.NetUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class LoginInActivity extends Activity implements OnClickListener{

	protected static final int NEED_CAPTCHA = 0;
	protected static final int NOT_NEED_CHPTCHA = 1;
	protected static final int GET_CAPTCHA_FAILD = 2;
	protected static final int LOGIN_SUCCESS = 3;
	protected static final int LOGIN_FAILD = 4;
	private EditText mEmailEdit;
	private EditText mPwdEdit;
	private LinearLayout captchaLinearLayout;
	private EditText mEditTextCaptchaValue;
	private ImageView mImageView;
	private Button btnLogin;
	private Button btnExit;
	private ProgressDialog pd;
	private String result=null;
	
	private Handler handle=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			pd.dismiss();
			switch(msg.what){
			case NEED_CAPTCHA:
				captchaLinearLayout.setVisibility(View.VISIBLE);
				Bitmap bitmap=(Bitmap) msg.obj;
				mImageView.setImageBitmap(bitmap);
				break;
			case NOT_NEED_CHPTCHA:
				break;
			case GET_CAPTCHA_FAILD:
				Toast.makeText(getApplicationContext(), "��ѯ��֤��ʧ��", 1).show();
				break;
			case LOGIN_SUCCESS:
				finish();
				break;
			case LOGIN_FAILD:
				Toast.makeText(getApplicationContext(), "��¼ʧ��", 1).show();
			}
			
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		// Ѱ�ҿؼ�
		setupView();
		// ��ֵ�ؼ�
		// ע��ؼ��������
		setListener();
		
	}

	/**
	 * ���ؿؼ�
	 */
	private void setupView() {
		
		mEmailEdit = (EditText) this.findViewById(R.id.EditTextEmail);
		mPwdEdit = (EditText) this.findViewById(R.id.EditTextPassword);
		captchaLinearLayout = (LinearLayout) this.findViewById(R.id.Captcha);
		mEditTextCaptchaValue=(EditText) this.findViewById(R.id.EditTextCaptchaValue);
		mImageView=(ImageView) this.findViewById(R.id.ImageViewCaptcha);
		btnLogin=(Button) this.findViewById(R.id.btnLogin);
		btnExit=(Button) this.findViewById(R.id.btnExit);
		//�ж��Ƿ���Ҫ��֤�� ����isNeedCaptcha()
		getCaptcha();
	}

	/**
	 * ��ȡ��֤��
	 */
	private void getCaptcha() {
		
		//����һ�����ȶԻ���
		pd=new ProgressDialog(this);
		pd.setMessage("���ڼ�����֤��");
		pd.show();
		new Thread(new Runnable(){
			@Override
			public void run() {
				Message msg=new Message();
				try {
					result=NetUtil.isNeedCaptcha();
					if(result!=null){
						//�����߳�������֤��ͼƬ Ȼ��ͼƬ���õ�mImageView
						String imagePath=getResources().getString(R.string.captchaurl)+result+"&amp;size=s";
						Bitmap map=NetUtil.getImage(imagePath);
						msg.what=NEED_CAPTCHA;
						msg.obj=map;
						handle.sendMessage(msg);
						
					} else{
						msg.what=NOT_NEED_CHPTCHA;
						handle.sendMessage(msg);
					}
				} catch (Exception e) {
					
					e.printStackTrace();
					msg.what=GET_CAPTCHA_FAILD;
					handle.sendMessage(msg);
				}
				
				
				
			}}).start();
	}

	//ע�����¼�
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btnLogin:
			String name =mEmailEdit.getText().toString();
			String pwd =mPwdEdit.getText().toString();
			//Log.d("LoginInActivity",result);
			System.out.println(result);
			if("".equals(name)||"".equals(pwd)){

				Toast.makeText(this, "�û��������벻��Ϊ��", Toast.LENGTH_SHORT).show();
				return;
			} else {
				//�ж��Ƿ���Ҫ��֤��
				if(result!=null){
					String captchaValue= mEditTextCaptchaValue.getText().toString();
					if("".equals(captchaValue)){
						Toast.makeText(this, "��֤�벻��Ϊ��", Toast.LENGTH_SHORT).show();
						return;
					} else {
						Log.d("LoginInActivity",name);
						Log.d("LoginInActivity",pwd);
						Log.d("LoginInActivity",result);
						Log.d("LoginInActivity",captchaValue);
						loginIn(name,pwd,captchaValue);
					}
				} else {
					
					loginIn(name,pwd,"");
				}
					
			}
			break;
		case R.id.btnExit:
			finish();
			break;
		case R.id.ImageViewCaptcha:
			getCaptcha();
		default:
			break;
		}
		
	}
	//���õ���¼�
	private void setListener(){
		btnLogin.setOnClickListener(this);
		btnExit.setOnClickListener(this);
		mImageView.setOnClickListener(this);
	}
	
	private void loginIn(final String name, final String pwd,final String captchavalue){
		
		//ִ�е�¼����
		pd.setMessage("���ڵ�¼");
		pd.show();
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg=new Message();
				try {
					boolean flag=NetUtil.login(name, pwd, captchavalue, result, getApplicationContext());
					if(flag){
						msg.what=LOGIN_SUCCESS;
						
					} else {
						msg.what=LOGIN_FAILD;
						
					}
					handle.sendMessage(msg);
				} catch (Exception e) {
					
					msg.what=LOGIN_FAILD;
					handle.sendMessage(msg);
					e.printStackTrace();
				}
				
			}}).start();
	}
}
