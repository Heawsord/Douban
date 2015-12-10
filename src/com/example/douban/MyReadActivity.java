package com.example.douban;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.douban.domain.Book;
import com.example.douban.util.LoadImageAsyncTask;
import com.example.douban.util.LoadImageAsyncTask.LoadImageAsyncTaskCallback;
import com.google.gdata.data.Link;
import com.google.gdata.data.douban.Attribute;
import com.google.gdata.data.douban.CollectionEntry;
import com.google.gdata.data.douban.CollectionFeed;
import com.google.gdata.data.douban.Subject;
import com.google.gdata.data.douban.SubjectEntry;
import com.google.gdata.data.douban.UserEntry;
import com.google.gdata.data.extensions.Rating;
import com.google.gdata.util.ServiceException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyReadActivity extends BaseActivity implements OnItemClickListener {
	private ListView subjectListView;
	private MyReadAdapter adapter;
	int startIndex;
	int count;
	int max=20;
	boolean isLoading=false;
	Map<String, SoftReference<Bitmap>> iconCache;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subject);
		startIndex=1;
		count=5;
		// ��ʼ���ڴ滺��
		iconCache = new HashMap<String, SoftReference<Bitmap>>();
		setupView();
		fillData();
		setListener();
	}

	@Override
	public void setupView() {
		mRelativeLoading = (RelativeLayout) this.findViewById(R.id.loading);
		subjectListView = (ListView) this.findViewById(R.id.subjectlist);

	}

	@Override
	public void setListener() {
		subjectListView.setOnItemClickListener(this);
		subjectListView.setOnScrollListener(new OnScrollListener(){

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch(scrollState){
				case OnScrollListener.SCROLL_STATE_IDLE:
					//�����ǰ״̬Ϊ��ֹ״̬������listview���һ���ɼ���Ŀ���������������һ����Ŀ
					int position=view.getLastVisiblePosition();
					Log.d("MyReadActivity","���һ���ɼ���λ��"+position);
					int count=adapter.getCount();
					System.out.println("listview item number"+count);
					if(position==(count-1)){
						startIndex=startIndex+count;
						if(startIndex>max){
							showToast("���ص������Ŀ");
							return;
						}
						if(isLoading) {
							return;
						}
						fillData();
					}
					break;
					
				}
				
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}});

	}

	@Override
	public void fillData() {
		// ͨ���첽�����ȡ���ݼ��ظ�listView
		new AsyncTask<Void, Void, List<Book>>() {
			@Override
			protected List<Book> doInBackground(Void... params) {
				UserEntry ue;
				try {
					ue = myService.getAuthorizedUser();
					String uid = ue.getUid();
					CollectionFeed feeds = myService.getUserCollections(uid,
							"book", null, null, startIndex, count);
					List<Book> books = new ArrayList<Book>();
					for (CollectionEntry ce : feeds.getEntries()) {
						Book book = new Book();
						Subject se = ce.getSubjectEntry();
						if (se != null) {
							String title = se.getTitle().getPlainText();
							book.setTitle(title);
							StringBuilder sb = new StringBuilder();
							for (Attribute attr : se.getAttributes()) {
								if ("author".equals(attr.getName())) {
									sb.append(attr.getContent());
									sb.append("/");
								} else if ("publisher".equals(attr.getName())) {
									sb.append(attr.getContent());
									sb.append("/");
								} else if ("pubdate".equals(attr.getName())) {
									sb.append(attr.getContent());
									sb.append("/");
								} else if ("isbn13".equals(attr.getName())) {
									sb.append(attr.getContent());

								}
							}
							book.setDescription(sb.toString());
							// ��ȡͼ������image
							for (Link link : se.getLinks()) {
								if ("image".equals(link.getRel())) {
									book.setBookUrl(link.getHref());
								}
							}
							// ��ȡͼ������
							Rating rating = se.getRating();
							if (rating != null) {
								book.setRating(rating.getAverage());
							}
							books.add(book);
						}
					}

					return books;

				} catch (Exception e) {

					e.printStackTrace();
					return null;
				}

			}

			@Override
			protected void onPreExecute() {
				// ��ʾ���ڼ��صĽ�����
				showLoading();
				isLoading=true;
				super.onPreExecute();
			}

			@Override
			protected void onPostExecute(List<Book> result) {
				// ���ݼ�����ɣ����ؽ�����
				hideLoading();
				isLoading=false;
				super.onPostExecute(result);
				if (result != null) {
					if(adapter==null){
						adapter = new MyReadAdapter(result);
						subjectListView.setAdapter(adapter);
					}else {
						//���»�ȡ�������ݼ��ص�������������
						//֪ͨlistview��������
						adapter.addMoreBook(result);
						adapter.notifyDataSetChanged();
					}
				} else {
					showToast("��Ŀ��Ϣ����ʧ��");
				}

			}
		}.execute();

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	private class MyReadAdapter extends BaseAdapter {

		private List<Book> books;

		public MyReadAdapter(List<Book> books) {
			this.books = books;

		}

		public void addMoreBook(List<Book> books){
			for(Book book:books){
				this.books.add(book);
			}
		}
		@Override
		public int getCount() {

			return books.size();
		}

		@Override
		public Object getItem(int position) {

			return books.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			//��convertView�����غõĲ��ֽ��л���
			if(convertView==null){
				view = View.inflate(MyReadActivity.this, R.layout.book_item,
						null);
			}else {
				view=convertView;
			}
			final ImageView iv_book = (ImageView) view
					.findViewById(R.id.book_img);
			RatingBar rb = (RatingBar) view.findViewById(R.id.ratingbar);
			TextView tv_title = (TextView) view.findViewById(R.id.book_title);
			TextView tv_description = (TextView) view
					.findViewById(R.id.book_description);
			Book book = books.get(position);
			if (book.getRating() != 0) {
				rb.setRating(book.getRating());
			} else {
				rb.setVisibility(View.INVISIBLE);
			}
			tv_description.setText(book.getDescription());
			tv_title.setText(book.getTitle());
			// �ж��ļ��Ƿ���sd����
			String bookUrl = book.getBookUrl();
			final String fileName = bookUrl.substring(
					bookUrl.lastIndexOf("/") + 1, bookUrl.length());
			// �ж��ļ������Ƿ����
			// File file =new
			// File(Environment.getExternalStorageDirectory(),fileName);
			// if(file.exists()){
			// iv_book.setImageURI(Uri.fromFile(file));
			// Log.d("MyReadActivity","ʹ��sd������ͼƬ");
			// } else {
			if (iconCache.containsKey(fileName)) {
				SoftReference<Bitmap> softRef = iconCache.get(fileName);
				if (softRef != null) {
					Bitmap bitmap = softRef.get();
					if (bitmap != null) {
						Log.d("MyReadActivity", "ʹ���ڴ滺��");
						iv_book.setImageBitmap(bitmap);
					} else {
						asyncLoadImage(iv_book, book, fileName);
					}
				}
			} else {
				asyncLoadImage(iv_book, book, fileName);
			}
			return view;
		}

		/**
		 * @param iv_book
		 *            ��ʾͼƬ��ImageView
		 * @param book
		 *            Book����
		 * @param fileName
		 *            ͼ��ImageUrl
		 */
		private void asyncLoadImage(final ImageView iv_book, Book book,
				final String fileName) {

			LoadImageAsyncTask task = new LoadImageAsyncTask(
					new LoadImageAsyncTaskCallback() {
						@Override
						public void beforeLoadImage() {
							iv_book.setImageResource(R.drawable.book);
						}

						@Override
						public void afterLoadImage(Bitmap bitmap) {

							if (bitmap != null) {
								iv_book.setImageBitmap(bitmap);
								// FileOutputStream stream;
								// try {
								// File file=new
								// File(Environment.getExternalStorageDirectory(),fileName);
								// stream = new FileOutputStream(file);
								// bitmap.compress(CompressFormat.JPEG, 100,
								// stream);
								// } catch (Exception e) {
								// e.printStackTrace();
								// }
								// ��bitmap�浽�ڴ滺����
								iconCache.put(fileName,
										new SoftReference<Bitmap>(bitmap));

							} else {
								iv_book.setImageResource(R.drawable.book);
							}
						}
					});
			task.execute(book.getBookUrl());
		}

	}

}
