package dd.trafficinfo.core;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BitmapLruCache;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class DownloadManagerComponent {

	private ImageLoader imageLoader;
	private RequestQueue mRequestQueue;
	
	
	public static DownloadManagerComponent getInstance(Context c) {

		return new DownloadManagerComponent(c);
	}
	
	
	public DownloadManagerComponent(Context c) {
		mRequestQueue = Volley.newRequestQueue(c);
		imageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(200));
	}
	
	public ImageLoader getImageLoader() {
		return imageLoader;
	}
	
	public RequestQueue getRequestQueue() {
		return mRequestQueue;
	}
	
	
}
