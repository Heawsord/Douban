package com.example.douban.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;


/**
 * 第一个参数是下载url
 * 第二个参数是下载进度
 * 第三个参数是任务执行完之后的返回值
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
	 * 后台子线程
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
