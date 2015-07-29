package TwitterService;

import java.io.Closeable;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.crowdmix.twitter.HomeActivity.BitmapCache;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Pair;
import android.widget.ImageView;

public class LoadImageAsyncTask extends AsyncTask<Void, Void, Pair<Bitmap, Exception>> {
    private ImageView mImageView;
    private String mUrl;
    private BitmapCache mCache;
    
    ImageDownloadComplete mcallBack = null;
    
    public interface ImageDownloadComplete
    {
    	public void onBitmapDownloadComplete(Bitmap pBitmap);
    }

    public LoadImageAsyncTask(BitmapCache cache, ImageView imageView, String url,ImageDownloadComplete pCallBack) {
        mCache = cache;
        mImageView = imageView;
        mUrl = url;
        mcallBack = pCallBack;
        
        if(mImageView != null)
        	mImageView.setTag(mUrl);
    }

    @Override
    protected void onPreExecute() {
    	
    	if(mImageView != null)
    	{
	        Bitmap bm = (mCache != null)?mCache.get(mUrl):null;
	
	        if(bm != null) {
	            cancel(false);
	
	            mImageView.setImageBitmap(bm);
	        }
    	}
    }

    @Override
    protected Pair<Bitmap, Exception> doInBackground(Void... arg0) {
        if(isCancelled()) {
            return null;
        }

        URL url;
        InputStream inStream = null;
        try {
            url = new URL(mUrl);
            URLConnection conn = url.openConnection();

            inStream = conn.getInputStream();

            Bitmap bitmap = BitmapFactory.decodeStream(inStream);

            return new Pair<Bitmap, Exception>(bitmap, null);

        } catch (Exception e) {
            return new Pair<Bitmap, Exception>(null, e);
        }
        finally {
            closeSilenty(inStream);
        }
    }

    @Override
    protected void onPostExecute(Pair<Bitmap, Exception> result) {
        if(result == null) {
        	 if(mcallBack != null)
             	mcallBack.onBitmapDownloadComplete(null);
            return;
        }

        if(result.first != null )
        {
        	if(mImageView != null && mUrl.equals(mImageView.getTag()))
        	{
        		if(mCache != null)
        			mCache.put(mUrl, result.first);
	            mImageView.setImageBitmap(result.first);
        	}
        
            if(mcallBack != null)
            	mcallBack.onBitmapDownloadComplete(result.first);
        }
        else
        {
        	 if(mcallBack != null)
             	mcallBack.onBitmapDownloadComplete(null);
        }
    }

    public void closeSilenty(Closeable closeable) {
        if(closeable != null) {
        try {
            closeable.close();
        } catch (Exception e) {
            // TODO: Log this
        }
        }
    }
}