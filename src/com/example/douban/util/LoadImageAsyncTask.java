package com.example.douban.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;


/**
 * ��һ������������url
 * �ڶ������������ؽ���
 * ����������������ִ����֮��ķ���ֵ
 * @author James
 *
 */
public class LoadImageAsyncTask extends AsyncTask<String,Void,Bitmap>{

	LoadImageAsyncTaskCallback loadImageAsyncTaskCallback;
	
	public LoadImageAsyncTask(
			LoadImageAsyncTaskCallback loadImageAsyncTaskCallback) {

		this.loadImageAsyncTaskCallback = loadImageAsyncTaskCallback;
	}

	public interface LoadImageAsyncTaskCallback{
		public void beforeLoadImage();
		public void afterLoadImage(Bitmap bitmap);
	}

	


	@Override
	protected void onPreExecute() {
		loadImageAsyncTaskCallback.beforeLoadImage();
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		loadImageAsyncTaskCallback.afterLoadImage(result); 
		super.onPostExecute(result);
	}

	/**
	 * ��̨���߳�
	 */
	@Override
	protected Bitmap doInBackground(String... params) {
		String path =params[0];
		try {
			URL url=new URL(path);
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			InputStream in=conn.getInputStream();
			return BitmapFactory.decodeStream(in);
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
		
	}

	
}
